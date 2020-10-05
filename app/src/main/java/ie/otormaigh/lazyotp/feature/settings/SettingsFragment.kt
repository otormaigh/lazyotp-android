package ie.otormaigh.lazyotp.feature.settings

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.service.BatteryLevelService
import ie.otormaigh.lazyotp.service.SlackPostWorker
import ie.otormaigh.lazyotp.toolbox.WorkScheduler
import ie.otormaigh.lazyotp.toolbox.batteryWarningEnabled
import ie.otormaigh.lazyotp.toolbox.settingsPrefs

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    findPreference<Preference>("btnSendTest")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      WorkScheduler.oneTimeRequest<SlackPostWorker>(requireContext(), SlackPostWorker.smsCodeData("Test", "012345"))

      true
    }

    findPreference<Preference>("key_battery_warning")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      if (context?.settingsPrefs?.batteryWarningEnabled == true) {
        findPreference<Preference>("key_battery_threshold")?.isVisible = true
        BatteryLevelService.start(requireContext())
      } else {
        findPreference<Preference>("key_battery_threshold")?.isVisible = false
        BatteryLevelService.stop(requireContext())
      }

      true
    }
  }
}
