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
import ie.otormaigh.lazyotp.feature.addprovider.AddSmsProviderState
import ie.otormaigh.lazyotp.feature.addprovider.AddSmsProviderViewModel
import ie.otormaigh.lazyotp.feature.settings.SettingsActivity
import ie.otormaigh.lazyotp.toolbox.extension.hideKeyboard
import ie.otormaigh.lazyotp.toolbox.extension.showKeyboard
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
  private val showInputConstraintSet by lazy {
    ConstraintSet().apply {
      clone(clMain)
      setVisibility(R.id.tilProvider, ConstraintSet.VISIBLE)
      setVisibility(R.id.tilDigitCount, ConstraintSet.VISIBLE)
    }
  }
  private val hideInputConstraintSet by lazy {
    ConstraintSet().apply {
      clone(clMain)
      setVisibility(R.id.tilProvider, ConstraintSet.GONE)
      setVisibility(R.id.tilDigitCount, ConstraintSet.GONE)
    }
  }
  private val viewModel by lazy { AddSmsProviderViewModel(app.database) }
  override val coroutineContext: CoroutineContext
    get() = Job()

  private val recyclerAdapter by lazy { SmsProviderRecyclerAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    viewModel.stateMachine.observe(this, Observer { processState(it) })

    rvSmsProvider.apply {
      adapter = recyclerAdapter
      layoutManager = LinearLayoutManager(this@MainActivity)
    }

    fabAdd.setOnLongClickListener {
      if (tilProvider.isVisible) processState(AddSmsProviderState.Success)
      true
    }
    fabAdd.setOnClickListener {
      if (tilProvider.isVisible) {
        viewModel.addProvider(etProvider.text, etDigitCount.text)
      } else {
        showInputConstraintSet.applyTo(clMain)
        tilProvider.requestFocus()
        tilProvider.showKeyboard()
      }
    }

    etDigitCount.setOnEditorActionListener { v, actionId, _ ->
      if (actionId == EditorInfo.IME_ACTION_DONE) fabAdd.performClick()
      false
    }

    app.database.smsCodeProviderDao().fetchAllLive().observe(this, Observer { data ->
      recyclerAdapter.submitList(data)
    })

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(
      arrayOf(
        Manifest.permission.RECEIVE_SMS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.READ_PHONE_NUMBERS
      ), 13
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
      is AddSmsProviderState.Fail.Sender -> tilProvider.error = state.reason
      is AddSmsProviderState.Fail.DigitCount -> tilDigitCount.error = state.reason
      is AddSmsProviderState.Success -> {
        tilProvider.hideKeyboard()
        hideInputConstraintSet.applyTo(clMain)
        tilProvider.error = null
        tilDigitCount.error = null
        etProvider.setText("")
        etDigitCount.setText("")
      }
    }
  }
}