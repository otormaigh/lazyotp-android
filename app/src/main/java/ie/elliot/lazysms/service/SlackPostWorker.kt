package ie.elliot.lazysms.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import ie.elliot.lazysms.BuildConfig
import ie.elliot.lazysms.api.Api
import ie.elliot.lazysms.toolbox.deviceName
import ie.elliot.lazysms.toolbox.settingsPrefs
import ie.elliot.lazysms.toolbox.slackToken

class SlackPostWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
  override suspend fun doWork(): Result {
      val deviceName = applicationContext.settingsPrefs.deviceName
      val sender = inputData.getString(ARG_SENDER)
      val smsCode = inputData.getString(ARG_SMS_CODE)
      val message = """{ "text":"*phone*: $deviceName\n*sender*: $sender\n*code*: $smsCode" }""".trimIndent()

      Api.instance.postSmsCode(
        message,
        applicationContext.settingsPrefs.slackToken.takeIf { it.isNotEmpty() } ?: BuildConfig.SLACK_TOKEN
      ).await()

    return Result.success()
  }

  companion object {
    private const val ARG_SMS_CODE = "arg.sms_code"
    private const val ARG_SENDER = "arg.sender"

    fun data(sender: String, smsCode: String) = Data.Builder().apply {
      putString(ARG_SENDER, sender)
      putString(ARG_SMS_CODE, smsCode)
    }.build()
  }
}