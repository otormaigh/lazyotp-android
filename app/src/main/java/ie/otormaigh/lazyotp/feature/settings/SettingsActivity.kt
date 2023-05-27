package ie.otormaigh.lazyotp.feature.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import ie.otormaigh.lazyotp.R

class SettingsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, SettingsFragment()).commit()

    supportActionBar?.apply {
      setDisplayHomeAsUpEnabled(true)
      title = getString(R.string.title_settings)
    }
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      android.R.id.home -> finish()
    }
    return super.onOptionsItemSelected(item)
  }
}