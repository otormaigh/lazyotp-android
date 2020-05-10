package ie.otormaigh.lazyotp.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import ie.otormaigh.lazyotp.BuildConfig
import ie.otormaigh.lazyotp.R
import ie.otormaigh.lazyotp.feature.MainActivity
import ie.otormaigh.lazyotp.toolbox.ForegroundServiceLauncher
import ie.otormaigh.lazyotp.toolbox.WorkScheduler
import ie.otormaigh.lazyotp.toolbox.batteryThreshold
import ie.otormaigh.lazyotp.toolbox.settingsPrefs
import timber.log.Timber

class BatteryLevelService : Service() {
  private val batteryLevelReceiver = object : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
      if (intent?.action != Intent.ACTION_BATTERY_CHANGED) return
      val batteryLevel = intent.extras?.getInt(BatteryManager.EXTRA_LEVEL) ?: -1

      if (batteryLevel > 0 && batteryLevel < application.settingsPrefs.batteryThreshold) {
        Timber.e("BatteryLevelService : batteryLevel -> $batteryLevel")

        WorkScheduler.oneTimeRequest<SlackPostWorker>(
          applicationContext,
          SlackPostWorker.lowBatteryData()
        )
      }
    }
  }

  override fun onBind(p0: Intent?): IBinder? = null

  override fun onCreate() {
    super.onCreate()

    val notificationIntent = Intent(this, MainActivity::class.java)

    val pendingIntent = PendingIntent.getActivity(
      this, 0,
      notificationIntent, 0
    )

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      val channel = NotificationChannel(
        CHANNEL_ID,
        getString(R.string.app_name),
        NotificationManager.IMPORTANCE_LOW
      )
      channel.setShowBadge(false)
      getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    val notification = NotificationCompat.Builder(this, CHANNEL_ID)
      .setSmallIcon(R.drawable.ic_notification_small)
      .setContentIntent(pendingIntent)
      .setContentText(getString(R.string.notif_content_text))
      .setStyle(NotificationCompat.BigTextStyle().bigText(getString(R.string.notif_content_text)))
      .setPriority(NotificationCompat.PRIORITY_LOW)
      .build()
    startForeground(BatteryLevelService::class.java.hashCode(), notification)

    LAUNCHER.onServiceCreated(this)

    registerReceiver(batteryLevelReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))
  }

  override fun onDestroy() {
    super.onDestroy()

    try {
      unregisterReceiver(batteryLevelReceiver)
    } catch (e: Exception) {
      Timber.e(e)
    }
  }

  companion object {
    private val LAUNCHER = ForegroundServiceLauncher(BatteryLevelService::class.java)
    private const val CHANNEL_ID = BuildConfig.APPLICATION_ID

    @JvmStatic
    fun start(context: Context) = LAUNCHER.startService(context)

    @JvmStatic
    fun stop(context: Context) = LAUNCHER.stopService(context)
  }
}