package ie.otormaigh.lazyotp.toolbox

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SmsCodeParserTest {
  @Test
  fun testParseAllNumbers() {
    assertThat(SmsCodeParser.parse("Numbers only 1234", 4))
      .isEqualTo("1234")
  }

  @Test
  fun testParseAllLetters() {
    // Uppercase - success
    assertThat(SmsCodeParser.parse("Letters only ABCD", 4))
      .isEqualTo("ABCD")

    // Lowercase - failure
    assertThat(SmsCodeParser.parse("Letters only abcd", 4))
      .isEqualTo("")
  }

  @Test
  fun testParseLetterNumberCode() {
    // One letter - success
    assertThat(SmsCodeParser.parse("Letters and numbers: 1A234.", 5))
      .isEqualTo("1A234")
    assertThat(SmsCodeParser.parse("Letters and numbers: 807C71.", 6))
      .isEqualTo("807C71")

    // Multiple letters and numbers - success
    assertThat(SmsCodeParser.parse("Letters and numbers: 4B37BE.", 6))
      .isEqualTo("4B37BE")

    // Lowercase - failure
    assertThat(SmsCodeParser.parse("Lowercase letters and numbers: 1a2345.", 6))
      .isEqualTo("")
  }
}