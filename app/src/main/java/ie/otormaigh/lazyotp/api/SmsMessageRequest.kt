package ie.otormaigh.lazyotp.api

import ie.otormaigh.lazyotp.BuildConfig
import java.util.concurrent.TimeUnit

class SmsMessageRequest(
  private val deviceName: String,
  private val sender: String?,
  private val smsCode: String?,
  private val sentTimestamp: Long
) {
  val jsonString: String?
    get() {
      if (sender.isNullOrEmpty()) return null
      if (smsCode.isNullOrEmpty()) return null
      if (sentTimestamp < (System.currentTimeMillis() - STALE_MESSAGE_CUTOFF)) return null

      return """
        {
          "text": "$smsCode",
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

  companion object {
    /**
     * See: https://github.com/otormaigh/lazyotp-android/issues/80
     */
    val STALE_MESSAGE_CUTOFF = TimeUnit.MINUTES.toMillis(15)
  }
}