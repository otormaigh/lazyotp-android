package ie.otormaigh.lazyotp.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import ie.otormaigh.lazyotp.BuildConfig
import ie.otormaigh.lazyotp.api.Api
import ie.otormaigh.lazyotp.toolbox.deviceName
import ie.otormaigh.lazyotp.toolbox.settingsPrefs
import ie.otormaigh.lazyotp.toolbox.slackToken

class SlackPostWorker(context: Context, workerParams: WorkerParameters) :
  CoroutineWorker(context, workerParams) {
  private val deviceName by lazy { applicationContext.settingsPrefs.deviceName }

  override suspend fun doWork(): Result {
    val message = when (inputData.getString(ARG_TYPE)) {
      TYPE_SMS_CODE -> createSmsMessage()
      TYPE_BATTERY_LEVEL -> createBatteryMessage()
      else -> throw UnsupportedOperationException("{${inputData.getString(ARG_TYPE)} is not supported}")
    }

    Api.instance.postSmsCode(
      message,
      applicationContext.settingsPrefs.slackToken.takeIf { it.isNotEmpty() }
        ?: BuildConfig.SLACK_TOKEN
    )

    return Result.success()
  }

  private fun createSmsMessage(): String {
    val sender = inputData.getString(ARG_SENDER)
    val smsCode = inputData.getString(ARG_SMS_CODE)
    return """
        {
          "attachments": [
            {
              "footer": "Version: ${BuildConfig.VERSION_NAME}",
              "fields": [
                {
                  "title" : "Phone",
                  "value" : "$deviceName",
                  "short" : true
                },
                {
                  "title" : "Sender",
                  "value" : "$sender",
                  "short" : true
                },
                {
                  "title" : "Code",
                  "value" : "$smsCode",
                  "short" : true
                }
              ]
            },
          ]
        }
    """.trimIndent()
  }

  private fun createBatteryMessage(): String = """
        {
          "attachments": [
            {
              "text": "My Battery Is Low and It’s Getting Dark",
              "footer": "Version: ${BuildConfig.VERSION_NAME}",
              "fields": [
                {
                  "title" : "Phone",
                  "value" : "$deviceName",
                  "short" : true
                }
              ]
            },
          ]
        }
    """.trimIndent()

  companion object {
    private const val ARG_TYPE = "arg.type"
    private const val ARG_SMS_CODE = "arg.sms_code"
    private const val ARG_SENDER = "arg.sender"

    private const val TYPE_BATTERY_LEVEL = "type.battery_level"
    private const val TYPE_SMS_CODE = "type.sms_code"

    fun smsCodeData(sender: String, smsCode: String) = Data.Builder().apply {
      putString(ARG_TYPE, TYPE_SMS_CODE)
      putString(ARG_SENDER, sender)
      putString(ARG_SMS_CODE, smsCode)
    }.build()

    fun lowBatteryData() = Data.Builder().apply {
      putString(ARG_TYPE, TYPE_BATTERY_LEVEL)
    }.build()
  }
}