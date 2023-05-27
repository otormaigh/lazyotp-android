package ie.otormaigh.lazyotp.feature.settings

import android.os.Bundle
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ie.otormaigh.lazyotp.BuildConfig
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.service.BatteryLevelService
import ie.otormaigh.lazyotp.service.SlackPostWorker
import ie.otormaigh.lazyotp.toolbox.WorkScheduler
import ie.otormaigh.lazyotp.toolbox.batteryWarningEnabled
import ie.otormaigh.lazyotp.toolbox.settingsPrefs
import ie.otormaigh.lazyotp.toolbox.slackToken

class SettingsFragment : PreferenceFragmentCompat() {
  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setBatteryThresholdItemVisibility()

    findPreference<Preference>("btnSendTest")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      if (context?.settingsPrefs?.slackToken.isNullOrEmpty()) {
        Toast.makeText(context, getString(R.string.slack_token_empty), Toast.LENGTH_LONG).show()
      } else {
        WorkScheduler.oneTimeRequest<SlackPostWorker>(
          requireContext(),
          SlackPostWorker.smsCodeData(
            "Test",
            "012345",
            System.currentTimeMillis()
          )
        )
      }

      true
    }

    findPreference<Preference>("key_battery_warning")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      setBatteryThresholdItemVisibility()
      true
    }

    findPreference<Preference>("tvAppVersion")?.summary = BuildConfig.VERSION_NAME
  }

  private fun setBatteryThresholdItemVisibility() {
    if (context?.settingsPrefs?.batteryWarningEnabled == true) {
      findPreference<Preference>("key_battery_threshold")?.isVisible = true
      BatteryLevelService.start(requireContext())
    } else {
      findPreference<Preference>("key_battery_threshold")?.isVisible = false
      BatteryLevelService.stop(requireContext())
    }
  }
}
