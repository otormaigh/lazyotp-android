package ie.otormaigh.lazyotp.di

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ie.otormaigh.lazyotp.LazyOtpDatabase
import ie.otormaigh.lazyotp.SmsCodeProviderQueries
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PersistenceModule {

  @Singleton
  @Provides
  fun provideSqlDriver(@ApplicationContext context: Context): SqlDriver =
    AndroidSqliteDriver(LazyOtpDatabase.Schema, context, "lazy-sms.db")

  @Singleton
  @Provides
  fun provideLazyOtpDatabase(driver: SqlDriver): LazyOtpDatabase =
    LazyOtpDatabase(driver)

  @Provides
  fun provideSmsCodeProviderQueries(database: LazyOtpDatabase): SmsCodeProviderQueries =
    database.smsCodeProviderQueries
}