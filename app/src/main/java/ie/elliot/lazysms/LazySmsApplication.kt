package ie.elliot.lazysms

import android.app.Application
import android.content.Context
import androidx.room.Room
import ie.elliot.lazysms.data.LazySmsDatabase
import timber.log.Timber

class LazySmsApplication : Application() {
  val database by lazy {
    Room.databaseBuilder(applicationContext, LazySmsDatabase::class.java, "lazy-sms.db")
      .allowMainThreadQueries()
      .apply { if (BuildConfig.DEBUG) fallbackToDestructiveMigration() }
      .build()
  }

  override fun onCreate() {
    super.onCreate()

    Timber.plant(object : Timber.DebugTree() {
      override fun createStackElementTag(element: StackTraceElement) =
        "(${element.fileName}:${element.lineNumber})"
    })
  }
}

val Context.app: LazySmsApplication
  get() = applicationContext as LazySmsApplication