package ie.elliot.lazysms.service

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import ie.elliot.lazysms.api.Api
import timber.log.Timber

class SlackPostWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
  override suspend fun doWork(): Result {
    val smsCode = inputData.getString(ARG_SMS_CODE)

    val payload = """{"text":"$smsCode"}""".trimIndent()
    Timber.e(payload)
    Api.instance.postSmsCode(payload).await()
    return Result.success()
  }

  companion object {
    const val ARG_SMS_CODE = "arg.sms_code"
  }
}