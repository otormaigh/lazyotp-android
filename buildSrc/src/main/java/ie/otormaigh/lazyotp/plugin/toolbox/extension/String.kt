package ie.otormaigh.lazyotp.plugin.toolbox.extension

import java.io.IOException
import java.util.concurrent.TimeUnit

fun String.runCommand(): String? {
  return try {
    /*
    \\s         // Split on whitespace
    (?=         // Followed by
      (?:       // Start a non-capture group
        [^\']*  // 0 or more non-quote characters
        \"      // 1 quote
        [^\']*  // 0 or more non-quote characters
        \'      // 1 quote
      )*        // 0 or more repetition of non-capture group (multiple of 2 quotes will be even)
      [^\']*    // Finally 0 or more non-quotes
      $         // Till the end  (This is necessary, else every comma will satisfy the condition)
    )           // End look-ahead
    */
    val parts = split(
      """\s(?=(?:[^\']*\'[^\']*\')*[^\']*$)""".toRegex())
      .map { it.replace("'", "") } // remove any single quotes
      .toList()

    val proc = ProcessBuilder(parts)
      .redirectOutput(ProcessBuilder.Redirect.PIPE)
      .redirectError(ProcessBuilder.Redirect.PIPE)
      .start()

    proc.waitFor(10, TimeUnit.MINUTES)
    proc.inputStream.bufferedReader().readText()
  } catch (e: IOException) {
    e.printStackTrace()
    null
  }
}