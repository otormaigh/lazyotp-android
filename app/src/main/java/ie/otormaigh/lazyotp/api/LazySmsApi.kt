package ie.otormaigh.lazyotp.api

import ie.otormaigh.lazyotp.BuildConfig
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

interface LazySmsApi {
  @POST("{token}")
  @FormUrlEncoded
  fun postSmsCode(
    @Field("payload") payload: String,
    @Path("token", encoded = true) token: String = BuildConfig.SLACK_TOKEN
  ): Deferred<Response<Unit>>
}