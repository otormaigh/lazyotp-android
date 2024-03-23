package ie.otormaigh.lazyotp.feature.settings

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.telephony.SmsMessage
import android.widget.Toast
import androidx.core.content.getSystemService
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import ie.otormaigh.lazyotp.BuildConfig
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.service.BatteryLevelService
import ie.otormaigh.lazyotp.service.SlackPostWorker
import ie.otormaigh.lazyotp.service.SmsReceiver
import ie.otormaigh.lazyotp.toolbox.WorkScheduler
import ie.otormaigh.lazyotp.toolbox.batteryWarningEnabled
import ie.otormaigh.lazyotp.toolbox.debugSMSInfoEnabled
import ie.otormaigh.lazyotp.toolbox.settingsPrefs
import ie.otormaigh.lazyotp.toolbox.slackToken
import java.text.SimpleDateFormat

class SettingsFragment : PreferenceFragmentCompat() {
  private var latestSms: SmsMessage? = null

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    addPreferencesFromResource(R.xml.prefs_settings)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setBatteryThresholdItemVisibility()
    setDebugSMSInfoItemVisibility()

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

    findPreference<Preference>("key_switch_debug_sms_info")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      setDebugSMSInfoItemVisibility()

      true
    }

    findPreference<Preference>("btnCopySender")?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
      context?.getSystemService<ClipboardManager>()
        ?.setPrimaryClip(ClipData.newPlainText("LazyOTP sender", latestSms?.originatingAddress))
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

  private fun setDebugSMSInfoItemVisibility() {
    findPreference<Preference>("key_debug_sms_info")?.summary = "SMS message details will appear here."

    if (context?.settingsPrefs?.debugSMSInfoEnabled == true) {
      findPreference<Preference>("key_debug_sms_info")?.isVisible = true
      SmsReceiver.onMessageReceived.observe(this) { smsMessage ->
        latestSms = smsMessage

        if (smsMessage != null) {
          findPreference<Preference>("btnCopySender")?.isVisible = true
          findPreference<Preference>("key_debug_sms_info")?.summary = """
          From: ${smsMessage.displayOriginatingAddress}
          Message: ${smsMessage.messageBody}
          Received At: ${SimpleDateFormat("hh:mm:ss").format(smsMessage.timestampMillis)}
        """.trimIndent()
        } else {
          findPreference<Preference>("btnCopySender")?.isVisible = false
        }
      }
    } else {
      findPreference<Preference>("key_debug_sms_info")?.isVisible = false
      findPreference<Preference>("btnCopySender")?.isVisible = false
    }
  }
}
