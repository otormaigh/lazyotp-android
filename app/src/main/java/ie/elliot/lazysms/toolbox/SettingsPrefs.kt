package ie.elliot.lazysms.toolbox

import android.content.Context
import android.content.SharedPreferences
import ie.elliot.lazysms.BuildConfig

val Context.settingsPrefs: SharedPreferences
  get() = getSharedPreferences("${BuildConfig.APPLICATION_ID}_preferences", Context.MODE_PRIVATE)

val SharedPreferences.slackToken: String get() = getString("key_slack_token", null) ?: BuildConfig.SLACK_TOKEN
val SharedPreferences.deviceName: String get() = getString("key_device_name", null) ?: "unknown"