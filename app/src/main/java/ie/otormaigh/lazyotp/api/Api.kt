package ie.otormaigh.lazyotp.api

import ie.otormaigh.lazyotp.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

object Api {
  private val okhttp = OkHttpClient.Builder()
    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
      level = HttpLoggingInterceptor.Level.NONE
      if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
    })

  private val retrofit = Retrofit.Builder()
    .baseUrl("https://hooks.slack.com/services/")
    .client(okhttp.build())
    .build()

  val instance = retrofit.create(LazySmsApi::class.java)
}