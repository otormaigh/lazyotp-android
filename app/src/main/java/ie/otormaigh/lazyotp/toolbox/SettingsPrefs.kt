package ie.otormaigh.lazyotp.toolbox

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import ie.otormaigh.lazyotp.BuildConfig

val Context.settingsPrefs: SharedPreferences
  get() = getSharedPreferences("${BuildConfig.APPLICATION_ID}_preferences", Context.MODE_PRIVATE)

val SharedPreferences.slackToken: String get() = getString("key_slack_token", null) ?: ""
val SharedPreferences.deviceName: String get() = getString("key_device_name", null) ?: "unknown"
val SharedPreferences.batteryThreshold: Int get() = getString("key_battery_threshold", "-1")?.toInt() ?: -1
val SharedPreferences.batteryWarningEnabled: Boolean get() = getBoolean("key_battery_warning", false)
val SharedPreferences.debugSMSInfoEnabled: Boolean get() = getBoolean("key_switch_debug_sms_info", false)
var SharedPreferences.batteryWarningSent: Boolean
  get() = getBoolean("key_battery_warning_sent", false)
  set(value) = edit {
    putBoolean("key_battery_warning_sent", value)
  }