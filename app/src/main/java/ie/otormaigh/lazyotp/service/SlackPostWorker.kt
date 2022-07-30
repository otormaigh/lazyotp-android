package ie.otormaigh.lazyotp.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import ie.otormaigh.lazyotp.BuildConfig
import ie.otormaigh.lazyotp.api.Api
import ie.otormaigh.lazyotp.api.BatteryMessageRequest
import ie.otormaigh.lazyotp.api.SmsMessageRequest
import ie.otormaigh.lazyotp.toolbox.deviceName
import ie.otormaigh.lazyotp.toolbox.settingsPrefs
import ie.otormaigh.lazyotp.toolbox.slackToken
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import timber.log.Timber

class SlackPostWorker(context: Context, workerParams: WorkerParameters) :
  CoroutineWorker(context, workerParams) {
  private val deviceName by lazy { applicationContext.settingsPrefs.deviceName }

  override suspend fun doWork(): Result {
    try {
      val message = when (inputData.getString(ARG_TYPE)) {
        TYPE_SMS_CODE -> createSmsMessage()
        TYPE_BATTERY_LEVEL -> createBatteryMessage()
        else -> throw UnsupportedOperationException("{${inputData.getString(ARG_TYPE)} is not supported}")
      }

      if (message.isNullOrEmpty()) return Result.failure()
      else sendMessage(message)

    } catch (e: Exception) {
      Timber.e(e)
    }

    return Result.success()
  }

  private suspend fun sendMessage(message: String) {
    Api.instance.postSmsCode(
      message.toRequestBody("application/json".toMediaTypeOrNull()),
      applicationContext.settingsPrefs.slackToken.takeIf { it.isNotEmpty() }
        ?: BuildConfig.SLACK_TOKEN
    )
  }

  private fun createSmsMessage(): String? {
    val sender = inputData.getString(ARG_SENDER)
    val smsCode = inputData.getString(ARG_SMS_CODE)
    val sentTimestamp = inputData.getLong(ARG_TIMESTAMP, -1)

    return SmsMessageRequest(deviceName, sender, smsCode, sentTimestamp).jsonString
  }

  private fun createBatteryMessage(): String =
    BatteryMessageRequest(deviceName).jsonString

  companion object {
    private const val ARG_TYPE = "arg.type"
    private const val ARG_SMS_CODE = "arg.sms_code"
    private const val ARG_SENDER = "arg.sender"
    private const val ARG_TIMESTAMP = "arg.sent_timestamp"

    private const val TYPE_BATTERY_LEVEL = "type.battery_level"
    private const val TYPE_SMS_CODE = "type.sms_code"

    fun smsCodeData(sender: String, smsCode: String, sentTimestamp: Long) = Data.Builder().apply {
      putString(ARG_TYPE, TYPE_SMS_CODE)
      putString(ARG_SENDER, sender)
      putString(ARG_SMS_CODE, smsCode)
      putLong(ARG_TIMESTAMP, sentTimestamp)
    }.build()

    fun lowBatteryData() = Data.Builder().apply {
      putString(ARG_TYPE, TYPE_BATTERY_LEVEL)
    }.build()
  }
}