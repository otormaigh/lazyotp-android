package ie.otormaigh.lazyotp.plugin

import ie.otormaigh.lazyotp.plugin.toolbox.Git

/**
 * Calculate the project version name and code base on the format:
 *
 * versionName : "major.minor.build-shortGitHash".
 * versionCode: An incrementing Integer based off the 'versionName'.
 */
object SemVer {
  private const val major = 0
  private const val minor = 2
  private val build: Int
    get() = if (isCi) Integer.parseInt(System.getProperty("CIRCLE_BUILD_NUM"))
    else 0
  private val isCi: Boolean
    get() = System.getProperty("CI") == "true"

  @JvmStatic
  val name: String
    get() {
      val versionName = "$major.$minor.$build-${Git.shortHash}"

      return if (isCi) versionName
      else "$versionName-local"
    }

  @JvmStatic
  val code: Int
    get() = major * 10000000 + minor * 100000 + build
}