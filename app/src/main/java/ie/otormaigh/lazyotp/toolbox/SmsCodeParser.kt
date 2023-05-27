package ie.otormaigh.lazyotp.toolbox

object SmsCodeParser {
  fun parse(message: String, codeLength: String): String {
    val pattern = """\b[A-Z0-9]{codeLength}\b"""
      .replace("codeLength", codeLength)
      .toPattern()

    val matcher = pattern.matcher(message)
    return if (matcher.find()) matcher.group(0) ?: ""
    else ""
  }

  fun parseCodeLengthFromMessage(message: String): String? {
    for (i in 1..message.length) {
      val codeLength = i.toString()
      val code = parse(message, codeLength)
      if (code.isNotEmpty()) {
        return codeLength
      }
    }
    return null
  }
}