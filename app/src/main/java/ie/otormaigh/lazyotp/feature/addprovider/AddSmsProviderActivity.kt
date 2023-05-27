package ie.otormaigh.lazyotp.feature.addprovider

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.provider.Telephony
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import dagger.hilt.android.AndroidEntryPoint
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.data.SmsProviderStore
import ie.otormaigh.lazyotp.databinding.ActivityAddProviderBinding
import ie.otormaigh.lazyotp.toolbox.SmsCodeParser
import ie.otormaigh.lazyotp.toolbox.extension.clear
import ie.otormaigh.lazyotp.toolbox.extension.clearError
import ie.otormaigh.lazyotp.toolbox.extension.setContentDescription
import javax.inject.Inject

@AndroidEntryPoint
class AddSmsProviderActivity : AppCompatActivity() {
  @Inject
  lateinit var smsProviderStore: SmsProviderStore

  private lateinit var binding: ActivityAddProviderBinding
  private val viewModel: AddSmsProviderViewModel by viewModels()

  private val senderArg: String by lazy { intent.getStringExtra(ARG_SENDER) ?: "" }
  private val digitCountArg: String by lazy { intent.getStringExtra(ARG_DIGIT_COUNT) ?: "" }

  private val viewType: ViewType
    get() = if (senderArg.isEmpty() && digitCountArg.isEmpty()) {
      ViewType.Add
    } else {
      ViewType.Update
    }

  private val smsReceiver by lazy {
    object : BroadcastReceiver() {
      override fun onReceive(context: Context?, intent: Intent?) {
        val message = Telephony.Sms.Intents.getMessagesFromIntent(intent).firstOrNull() ?: return
        val sender = message.displayOriginatingAddress
        val messageBody = message.displayMessageBody

        with(binding) {
          tvSenderTitle.isVisible = true
          tvSender.text = sender
          tvMessageTitle.isVisible = true
          tvMessage.text = messageBody
          tvDigitsTitle.isVisible = true
          tvDigits.text = SmsCodeParser.parseCodeLengthFromMessage(messageBody) ?: getString(R.string.unknown)
        }

        val code = SmsCodeParser.parse(messageBody, binding.etDigitCount.text.toString())
        if (smsProviderStore.isSenderKnown(sender) && code.isNotEmpty()) {
          showParseSuccess(code)
        } else {
          showParseFail()
        }
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityAddProviderBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupUI()
    setupObservers()
  }

  override fun onDestroy() {
    super.onDestroy()
    unregisterReceiver(smsReceiver)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_debug_helper, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> finish()
      R.id.menuSave -> saveInput()
    }
    return super.onOptionsItemSelected(item)
  }

  private fun setupUI(): Unit = with(binding) {
    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      title = getString(
        when (viewType) {
          ViewType.Add -> R.string.title_add_provider_add
          ViewType.Update -> R.string.title_add_provider_update
        }
      )
    }

    etProvider.addTextChangedListener(onTextChanged = { _, _, _, _ ->
      tilProvider.clearError()
      ivSuccessFail.setImageDrawable(null)
      tvCode.clear()
    })
    etProvider.setText(senderArg)
    etDigitCount.addTextChangedListener(onTextChanged = { _, _, _, _ ->
      tilDigitCount.clearError()
      ivSuccessFail.setImageDrawable(null)
      tvCode.clear()
    })
    etDigitCount.setText(digitCountArg)
    etDigitCount.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) {
        tilProvider.clearFocus()
        etProvider.clearFocus()
        tilDigitCount.clearFocus()
        etDigitCount.clearFocus()
      }

      false
    }
  }

  private fun setupObservers() {
    viewModel.stateMachine.observe(this, ::processState)
    registerReceiver(smsReceiver, IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION))
  }

  private fun processState(state: AddSmsProviderState): Unit = when (state) {
    is AddSmsProviderState.Fail.Sender -> with(binding.tilProvider) {
      requestFocus()
      error = getString(state.reason)
    }

    is AddSmsProviderState.Fail.DigitCount -> with(binding.tilDigitCount) {
      requestFocus()
      error = getString(state.reason)
    }

    is AddSmsProviderState.Success -> onBackPressedDispatcher.onBackPressed()
  }

  private fun showParseSuccess(code: String): Unit = with(binding) {
    ivSuccessFail.setImageResource(R.drawable.ic_success)
    ivSuccessFail.setContentDescription(R.string.content_desc_success_smiley_face)
    tvCode.text = code
  }

  private fun showParseFail(): Unit = with(binding) {
    ivSuccessFail.setImageResource(R.drawable.ic_failure)
    ivSuccessFail.setContentDescription(R.string.content_desc_fail_frowny_face)
    tvCode.clear()
  }

  private fun saveInput(): Unit = with(binding) {
    tilProvider.clearError()
    tilDigitCount.clearError()

    viewModel.upsertProvider(senderArg, etProvider.text, etDigitCount.text)
  }

  companion object {
    private const val ARG_SENDER = "arg.sender"
    private const val ARG_DIGIT_COUNT = "arg.digit_count"

    fun start(activity: Activity, sender: String = "", digitCount: String = "") {
      activity.startActivity(Intent(activity, AddSmsProviderActivity::class.java).apply {
        putExtra(ARG_SENDER, sender)
        putExtra(ARG_DIGIT_COUNT, digitCount)
      })
    }

    sealed class ViewType {
      object Add : ViewType()
      object Update : ViewType()
    }
  }
}