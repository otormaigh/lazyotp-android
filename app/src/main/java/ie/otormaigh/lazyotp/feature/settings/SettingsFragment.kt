package ie.otormaigh.lazyotp.feature.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.service.SlackPostWorker
import ie.otormaigh.lazyotp.toolbox.WorkScheduler

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    findPreference<Preference>("btnSendTest")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      WorkScheduler.oneTimeRequest<SlackPostWorker>(requireContext(), SlackPostWorker.data("Test", "012345"))

      true
    }
  }
}
