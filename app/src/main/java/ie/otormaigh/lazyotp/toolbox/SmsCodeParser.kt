package ie.otormaigh.lazyotp.toolbox

import java.util.regex.Pattern

object SmsCodeParser {
  fun parse(message: String, codeLength: Int): String {
    val pattern = Pattern.compile("(\\d{$codeLength})")
    val matcher = pattern.matcher(message)
    return if (matcher.find()) matcher.group(0) ?: ""
    else ""
  }
}