package ie.otormaigh.lazyotp.feature.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.otormaigh.lazyotp.R

class SettingsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, SettingsFragment()).commit()
  }
}