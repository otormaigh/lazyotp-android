package ie.otormaigh.lazyotp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
  entities = [
    SmsCodeProvider::class
  ],
  version = 1
)
abstract class LazySmsDatabase : RoomDatabase() {
  abstract fun smsCodeProviderDao(): SmsCodeproviderDao
}