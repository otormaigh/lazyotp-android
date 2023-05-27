package ie.otormaigh.lazyotp.api

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SmsMessageRequestTest {

  @Test
  fun testMessageSentNowIsNotStale() {
    val deviceName = "Test Device"
    val sender = "Sender"
    val smsCode = "123456"
    val sentTimestamp = System.currentTimeMillis()

    assertThat(SmsMessageRequest(deviceName, sender, smsCode, sentTimestamp).jsonString)
      .isNotNull()
  }

  @Test
  fun testMessageSentBeforeCutoffIsStale() {
    val deviceName = "Test Device"
    val sender = "Sender"
    val smsCode = "123456"
    val sentTimestamp = System.currentTimeMillis() - (SmsMessageRequest.STALE_MESSAGE_CUTOFF * 2)

    assertThat(SmsMessageRequest(deviceName, sender, smsCode, sentTimestamp).jsonString)
      .isNull()
  }
}