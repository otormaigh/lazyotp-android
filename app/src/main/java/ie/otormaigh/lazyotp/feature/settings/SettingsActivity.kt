package ie.otormaigh.lazyotp.feature.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ie.otormaigh.lazyotp.R

class SettingsActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainer, SettingsFragment()).commit()
  }

  companion object {
    fun start(activity: Activity) {
      activity.startActivity(Intent(activity, SettingsActivity::class.java))
    }
  }
}