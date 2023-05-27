pluginManagement {
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
  repositories {
    google()
    mavenCentral()
  }
}

plugins {
  id("com.gradle.enterprise").version("3.10.3")
}

gradleEnterprise {
  buildScan {
    termsOfServiceUrl = "https://gradle.com/terms-of-service"
    termsOfServiceAgree = if (System.getenv("CI") != null) "yes" else "no"
  }
}

rootProject.name = "lazyotp-andrid"
include(":app")
