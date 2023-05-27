package ie.otormaigh.lazyotp.feature

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.SmsCodeProviderQueries
import ie.otormaigh.lazyotp.databinding.ActivityMainBinding
import ie.otormaigh.lazyotp.feature.addprovider.AddSmsProviderActivity
import ie.otormaigh.lazyotp.feature.settings.SettingsActivity
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  @Inject
  lateinit var smsCodeProviderQueries: SmsCodeProviderQueries

  private lateinit var binding: ActivityMainBinding

  private val recyclerAdapter by lazy {
    SmsProviderRecyclerAdapter { provider ->
      AddSmsProviderActivity.start(this, provider.sender, provider.codeLength)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    setupUI()
  }

  override fun onResume() {
    super.onResume()
    recyclerAdapter.submitList(smsCodeProviderQueries.fetchAll().executeAsList())
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

  private fun setupUI(): Unit = with(binding) {
    rvSmsProvider.adapter = recyclerAdapter

    fabAdd.setOnClickListener {
      AddSmsProviderActivity.start(this@MainActivity)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) requestPermissions(
      arrayOf(Manifest.permission.RECEIVE_SMS), 13
    )
  }
}