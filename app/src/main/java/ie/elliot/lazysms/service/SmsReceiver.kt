package ie.elliot.lazysms.service

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import androidx.core.content.ContextCompat
import ie.elliot.lazysms.app
import ie.elliot.lazysms.toolbox.SmsCodeParser
import ie.elliot.lazysms.toolbox.WorkScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SmsReceiver : BroadcastReceiver(), CoroutineScope {
  override val coroutineContext: CoroutineContext get() = Job()

  override fun onReceive(context: Context, intent: Intent) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) return

    if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
      launch { parseMessage(context, intent) }
    }
  }

  private suspend fun parseMessage(context: Context, intent: Intent) {
    val smsCodeProviders = context.app.database.smsCodeProviderDao().fetchAllAsync()
    Telephony.Sms.Intents.getMessagesFromIntent(intent).forEach { smsMessage ->
      smsCodeProviders.firstOrNull { it.sender == smsMessage.displayOriginatingAddress }?.let {

        WorkScheduler.oneTimeRequest<SlackPostWorker>(
          SlackPostWorker.data(
            smsMessage.displayOriginatingAddress,
            SmsCodeParser.parse(smsMessage.messageBody, it.codeLength)
          )
        )
      }
    }
  }
}