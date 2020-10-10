package ie.otormaigh.lazyotp.plugin

import ie.otormaigh.lazyotp.plugin.toolbox.Git

/**
 * Calculate the project version name and code base on the format:
 *
 * versionName : "major.minor.patch-shortGitHash".
 * versionCode: An incrementing Integer based off the 'versionName'.
 */
object SemVer {
  private const val major = 0
  private const val minor = 2
  private const val patch = 1
  private val build: Int
    get() = if (isCi) Integer.parseInt(System.getProperty("CIRCLE_BUILD_NUM"))
    else 0
  private val isCi: Boolean
    get() = System.getProperty("CI").equals("true", true)

  @JvmStatic
  val name: String
    get() = "$major.$minor.$patch-${Git.shortHash}"

  @JvmStatic
  val code: Int
    get() = (major * 1_000_000 + minor * 1_000 + patch) + build
}