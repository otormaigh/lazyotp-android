package ie.otormaigh.lazyotp.plugin.toolbox

import ie.otormaigh.lazyotp.plugin.toolbox.extension.runCommand

object BuildConst {
  object Git {
    val shortHash = "git rev-parse --short HEAD".runCommand()?.trim() ?: ""
  }

  object Version {
    private const val major = 0
    private const val minor = 2
    private const val patch = 0
    private val build = System.getenv("CIRCLE_BUILD_NUM")?.toInt() ?: 1

    val name = "$major.$minor.$patch-${Git.shortHash}"
    val code = major * 10000000 + minor * 100000 + patch * 1000 + build
  }
}