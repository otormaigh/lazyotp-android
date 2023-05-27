package ie.otormaigh.lazyotp.data

import com.google.common.truth.Truth.assertThat
import ie.otormaigh.lazyotp.SmsCodeProvider
import ie.otormaigh.lazyotp.SmsCodeProviderQueries
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class SmsProviderStoreTest {
  private val queries: SmsCodeProviderQueries = mockk(relaxed = true)
  private lateinit var store: SmsProviderStore

  @Before
  fun setup() {
    store = SmsProviderStore(queries)
  }

  @Test
  fun testWhitespaceIsIgnored() {
    every { queries.fetchAll().executeAsList() } returns listOf(SmsCodeProvider("Test", "6"))

    assertThat(store.isSenderKnown("T E S T"))
      .isTrue()

    assertThat(store.isSenderKnown(" Test "))
      .isTrue()
  }
}