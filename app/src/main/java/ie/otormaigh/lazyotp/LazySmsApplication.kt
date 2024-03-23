package ie.otormaigh.lazyotp

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import ie.otormaigh.lazyotp.service.BatteryLevelService
import ie.otormaigh.lazyotp.toolbox.batteryWarningEnabled
import ie.otormaigh.lazyotp.toolbox.settingsPrefs
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class LazySmsApplication : Application(), Configuration.Provider {
  @Inject
  lateinit var workerFactory: HiltWorkerFactory

  override val workManagerConfiguration: Configuration
    get() = Configuration.Builder()
      .setWorkerFactory(workerFactory)
      .build()

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