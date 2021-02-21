package ie.otormaigh.lazyotp.feature

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.app
import ie.otormaigh.lazyotp.databinding.ActivityMainBinding
import ie.otormaigh.lazyotp.feature.addprovider.AddSmsProviderState
import ie.otormaigh.lazyotp.feature.addprovider.AddSmsProviderViewModel
import ie.otormaigh.lazyotp.feature.settings.SettingsActivity
import ie.otormaigh.lazyotp.toolbox.extension.hideKeyboard
import ie.otormaigh.lazyotp.toolbox.extension.showKeyboard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
  private lateinit var binding: ActivityMainBinding

  private val showInputConstraintSet by lazy {
    ConstraintSet().apply {
      clone(binding.clMain)
      setVisibility(R.id.tilProvider, ConstraintSet.VISIBLE)
      setVisibility(R.id.tilDigitCount, ConstraintSet.VISIBLE)
    }
  }
  private val hideInputConstraintSet by lazy {
    ConstraintSet().apply {
      clone(binding.clMain)
      setVisibility(R.id.tilProvider, ConstraintSet.GONE)
      setVisibility(R.id.tilDigitCount, ConstraintSet.GONE)
    }
  }
  private val viewModel by lazy { AddSmsProviderViewModel(app.database) }
  override val coroutineContext: CoroutineContext
    get() = Job()

  private val recyclerAdapter by lazy {
    SmsProviderRecyclerAdapter { provider ->
      binding.fabAdd.performClick()
      binding.etProvider.tag = provider.sender
      binding.etProvider.setText(provider.sender)
      binding.etDigitCount.setText(provider.codeLength.toString())
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    val view = binding.root
    setContentView(view)

    viewModel.stateMachine.observe(this, Observer { processState(it) })

    binding.rvSmsProvider.apply {
      adapter = recyclerAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    binding.fabAdd.setOnLongClickListener {
      if (binding.tilProvider.isVisible) processState(AddSmsProviderState.Success)
      true
    }
    binding.fabAdd.setOnClickListener {
      if (binding.tilProvider.isVisible) {
        if (binding.etProvider.tag == null) viewModel.addProvider(binding.etProvider.text, binding.etDigitCount.text)
        else viewModel.updateProvider(binding.etProvider.tag.toString(), binding.etProvider.text, binding.etDigitCount.text)
      } else {
        showInputConstraintSet.applyTo(binding.clMain)
        binding.tilProvider.requestFocus()
        binding.tilProvider.showKeyboard()
      }
    }

    binding.etDigitCount.setOnEditorActionListener { _, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) binding.fabAdd.performClick()
      false
    }

    app.database.smsCodeProviderDao().fetchAllLive().observe(this, Observer { data ->
      recyclerAdapter.submitList(data)
    })

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(
      arrayOf(Manifest.permission.RECEIVE_SMS), 13
    )
  }

  override fun onPause() {
    super.onPause()
    coroutineContext.cancel()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      R.id.menuSettings -> startActivity(Intent(this, SettingsActivity::class.java))
    }
    return super.onOptionsItemSelected(item)
  }

  private fun processState(state: AddSmsProviderState) {
    when (state) {
      is AddSmsProviderState.Fail.Sender -> binding.tilProvider.error = state.reason
      is AddSmsProviderState.Fail.DigitCount -> binding.tilDigitCount.error = state.reason
      is AddSmsProviderState.Success -> {
        binding.tilProvider.hideKeyboard()
        hideInputConstraintSet.applyTo(binding.clMain)
        binding.tilProvider.error = null
        binding.tilDigitCount.error = null
        binding.etProvider.tag = null
        binding.etProvider.setText("")
        binding.etDigitCount.setText("")
      }
    }
  }
}