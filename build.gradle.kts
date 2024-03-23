buildscript {
  repositories {
    google()
    mavenCentral()
  }
  dependencies {
    classpath(libs.android.build)
    classpath(libs.kotlin.plugin)
    classpath(libs.google.hilt.plugin)
  }
}

allprojects {
  repositories {
    google()
    mavenCentral()
  }
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

tasks.withType<Wrapper> {
  gradleVersion = "8.7"
  distributionType = Wrapper.DistributionType.BIN
}