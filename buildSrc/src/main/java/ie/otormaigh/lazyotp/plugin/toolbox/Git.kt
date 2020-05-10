package ie.otormaigh.lazyotp.plugin.toolbox

import ie.otormaigh.lazyotp.plugin.toolbox.extension.runCommand

object Git {
  val shortHash = "git rev-parse --short HEAD".runCommand()?.trim() ?: ""
}