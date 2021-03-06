package ie.otormaigh.lazyotp.api

import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface LazySmsApi {
  @POST("{token}")
  suspend fun postSmsCode(
    @Body payload: RequestBody,
    @Path("token", encoded = true) token: String
  ): Response<Unit>
}