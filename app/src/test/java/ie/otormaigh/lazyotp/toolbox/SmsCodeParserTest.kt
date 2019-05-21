package ie.otormaigh.lazyotp.toolbox

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SmsCodeParserTest {
  @Test
  fun testParse() {
    val input = "Lazy SMS code: 1234. This is your very special, totally unique code."
    val output = SmsCodeParser.parse(input, 4)

    assertThat(output)
      .isEqualTo("1234")
  }
}