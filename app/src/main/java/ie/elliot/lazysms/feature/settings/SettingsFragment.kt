package ie.elliot.lazysms.feature.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import ie.elliot.lazysms.R

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)
  }
}
