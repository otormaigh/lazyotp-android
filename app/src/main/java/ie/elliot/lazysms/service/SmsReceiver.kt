package ie.elliot.lazysms.service

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.work.Data
import ie.elliot.lazysms.app
import ie.elliot.lazysms.toolbox.SmsCodeParser
import ie.elliot.lazysms.toolbox.WorkScheduler
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

class SmsReceiver : BroadcastReceiver(), CoroutineScope {
  override val coroutineContext: CoroutineContext
    get() = Job()

  private val exceptionHhandler = CoroutineScope(CoroutineExceptionHandler { _, exception ->
    Timber.e(Log.getStackTraceString(exception))
  })

  override fun onReceive(context: Context, intent: Intent) {
    Timber.e("onReceive -> ${intent.action}")

    if (ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.RECEIVE_SMS
      ) != PackageManager.PERMISSION_GRANTED
    ) return
    if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
      launch {
        val smsCodeProviders = context.app.database.smsCodeProviderDao().fetchAllAsync()
        Telephony.Sms.Intents.getMessagesFromIntent(intent).forEach { smsMessage ->
          smsCodeProviders.firstOrNull { it.sender == smsMessage.displayOriginatingAddress }?.let {

            val smsCode = SmsCodeParser.parse(smsMessage.messageBody, it.codeLength)
            WorkScheduler.oneTimeRequest<SlackPostWorker>(Data.Builder().apply {
              putString(
                SlackPostWorker.ARG_SMS_CODE,
                smsCode
              )
            }.build())
          }
        }
      }
    }
  }
}