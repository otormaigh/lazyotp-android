package ie.otormaigh.lazyotp.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ie.otormaigh.lazyotp.BuildConfig
import ie.otormaigh.lazyotp.api.LazySmsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class NetworkingModule {
  @Provides
  fun provideOkHttp(): OkHttpClient =
    OkHttpClient.Builder()
      .addNetworkInterceptor(HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.NONE
        if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
      })
      .build()

  @Provides
  fun provideLazySmsApi(okhttp: OkHttpClient): LazySmsApi =
    Retrofit.Builder()
      .baseUrl("https://hooks.slack.com/services/")
      .client(okhttp)
      .build()
      .create(LazySmsApi::class.java)
}