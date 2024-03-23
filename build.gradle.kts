plugins {
  alias(libs.plugins.android.application) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.google.hilt) apply false
  alias(libs.plugins.google.ksp) apply false
}

tasks.register("clean", Delete::class) {
  delete(rootProject.buildDir)
}

tasks.withType<Wrapper> {
  gradleVersion = "8.7"
  distributionType = Wrapper.DistributionType.BIN
}