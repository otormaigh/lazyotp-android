package ie.otormaigh.lazyotp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ie.otormaigh.lazyotp.service.BatteryLevelService
import ie.otormaigh.lazyotp.toolbox.batteryWarningEnabled
import ie.otormaigh.lazyotp.toolbox.settingsPrefs
import timber.log.Timber

@HiltAndroidApp
class LazySmsApplication : Application() {
  override fun onCreate() {
    super.onCreate()

    if (BuildConfig.DEBUG) {
      Timber.plant(object : Timber.DebugTree() {
        override fun createStackElementTag(element: StackTraceElement) =
          "(${element.fileName}:${element.lineNumber})"
      })
    }

    if (settingsPrefs.batteryWarningEnabled) BatteryLevelService.start(this)
    else BatteryLevelService.stop(this)
  }
}