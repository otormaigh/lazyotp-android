package ie.elliot.lazysms.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import ie.elliot.lazysms.api.Api
import ie.elliot.lazysms.toolbox.extension.phoneNumber

class SlackPostWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
  override suspend fun doWork(): Result {
    val phoneNumber = applicationContext.phoneNumber
    val sender = inputData.getString(ARG_SENDER)
    val smsCode = inputData.getString(ARG_SMS_CODE)
    val message = """{ "text":"*phone*: $phoneNumber\n*sender*: $sender\n*code*: $smsCode" }""".trimIndent()

    Api.instance.postSmsCode(message).await()

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