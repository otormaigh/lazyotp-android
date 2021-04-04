package ie.otormaigh.lazyotp.toolbox

object SmsCodeParser {
  fun parse(message: String, codeLength: Int): String {
    val pattern = """\b[A-Z0-9]{codeLength}\b"""
      .replace("codeLength", codeLength.toString())
      .toPattern()

    val matcher = pattern.matcher(message)
    return if (matcher.find()) matcher.group(0) ?: ""
    else ""
  }
}