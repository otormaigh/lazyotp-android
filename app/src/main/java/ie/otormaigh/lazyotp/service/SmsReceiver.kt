package ie.otormaigh.lazyotp.service

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Telephony
import android.telephony.SmsMessage
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.AndroidEntryPoint
import ie.otormaigh.lazyotp.data.SmsProviderStore
import ie.otormaigh.lazyotp.toolbox.SmsCodeParser
import ie.otormaigh.lazyotp.toolbox.WorkScheduler
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SmsReceiver : BroadcastReceiver() {
  @Inject
  lateinit var smsProviderStore: SmsProviderStore

  override fun onReceive(context: Context, intent: Intent) {
    Timber.d("onReceive")
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) return

    if (intent.action == Telephony.Sms.Intents.SMS_RECEIVED_ACTION) {
      parseMessage(context, intent)
    }
  }

  private fun parseMessage(context: Context, intent: Intent) {
    Timber.d("Parsing message")

    Telephony.Sms.Intents.getMessagesFromIntent(intent).forEach { smsMessage ->
      Timber.d("SmsMessage -> ${smsMessage.messageBody}")
      Timber.d("From -> ${smsMessage.displayOriginatingAddress}")

      onMessageReceived.value = smsMessage

      smsProviderStore.fetchWithId(smsMessage.displayOriginatingAddress)?.let { provider ->
        WorkScheduler.oneTimeRequest<SlackPostWorker>(
          context,
          SlackPostWorker.smsCodeData(
            smsMessage.displayOriginatingAddress,
            SmsCodeParser.parse(smsMessage.messageBody, provider.codeLength),
            smsMessage.timestampMillis
          )
        )
      }
    }
  }

  companion object {
    val onMessageReceived: MutableLiveData<SmsMessage> = MutableLiveData()
  }
}