import ie.otormaigh.lazyotp.plugin.SemVer

plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  id("dagger.hilt.android.plugin")
  id("app.cash.sqldelight").version(libs.versions.sqldelight)
}

if (file("../enc.properties").exists()) {
  apply(from = "../enc.properties")
}

android {
  namespace = "ie.otormaigh.lazyotp"
  compileSdk = 33

  defaultConfig {
    applicationId = "ie.otormaigh.lazyotp"
    minSdk = 21
    targetSdk = 33
    versionCode = SemVer.code
    versionName = SemVer.name
    base.archivesName.set("lazyotp-$versionName")
    resourceConfigurations += listOf("en", "ga", "it", "es", "fr", "de", "pt")
  }

  sourceSets {
    getByName("main") { assets.srcDirs(files("src/main/sqldelight")) }
  }

  buildFeatures {
    buildConfig = true
    viewBinding = true
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }

  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_17.toString()
  }

  lint {
    error += "MissingTranslation"
    error += "HardcodedText"
  }

  signingConfigs {
    named("debug").configure {
      storeFile = file("../signing/debug.keystore")
    }
    if (file("../signing/release.keystore").exists()) {
      create("release") {
        storeFile = file("../signing/release.keystore")
        storePassword = project.properties["store_password"].toString()
        keyAlias = project.properties["key_alias"].toString()
        keyPassword = project.properties["key_password"].toString()
      }
    }
  }

  buildTypes {
    getByName("debug") {
      signingConfig = signingConfigs["debug"]
      applicationIdSuffix = ".debug"
    }

    getByName("release") {
      signingConfig = signingConfigs["debug"]
      postprocessing {
        proguardFile("proguard-rules.pro")
        isRemoveUnusedResources = true
        isRemoveUnusedCode = true
        isOptimizeCode = true
        isObfuscate = true
      }
    }
  }

  packagingOptions {
    exclude("META-INF/atomicfu.kotlin_module")
  }
}

sqldelight {
  databases {
    create("LazyOtpDatabase") {
      packageName.set("ie.otormaigh.lazyotp")
      schemaOutputDirectory.set(file("src/main/sqldelight/ie/otormaigh/lazyotp/databases"))
      verifyMigrations.set(true)
    }
  }
}

dependencies {
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.constraintlayout)
  implementation(libs.androidx.core)
  implementation(libs.androidx.preference)
  implementation(libs.androidx.viewmodel)
  implementation(libs.androidx.work.runtime)
  implementation(libs.androidx.test.runner)
  implementation(libs.androidx.test.rules)

  implementation(libs.google.material)
  implementation(libs.google.hilt.core)
  kapt(libs.google.hilt.compiler)

  implementation(libs.kotlin.stdlib)
  implementation(libs.kotlin.coroutine.core)
  implementation(libs.kotlin.coroutine.android)

  implementation(libs.square.okhttp.core)
  implementation(libs.square.okhttp.logging)
  implementation(libs.square.retrofit.core)
  implementation(libs.square.sqldelight.driver)

  implementation(libs.misc.timber)

  testImplementation(libs.google.truth)
  testImplementation(libs.misc.junit)
  testImplementation(libs.misc.mockk)
}

tasks.getByName("preBuild").dependsOn(":app:generateSqlDelightInterface")