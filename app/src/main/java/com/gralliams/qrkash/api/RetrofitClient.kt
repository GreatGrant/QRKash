package com.gralliams.qrkash.api

import android.util.Log
import com.gralliams.qrkash.BuildConfig
import com.gralliams.qrkash.BuildConfig.API_KEY
import com.gralliams.qrkash.constants.AUTHORIZATION
import com.gralliams.qrkash.constants.BASE_URL
import com.gralliams.qrkash.model.VirtualAccountRequest
import com.gralliams.qrkash.model.VirtualAccountResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

object RetrofitClient {

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
            redactHeader("Authorization")
        })
        .addInterceptor { chain ->
            val request = chain.request()

            // Log the request
            Log.d("Retrofit", "Sending ${request.method} request to: ${request.url}")

            val response = chain.proceed(request)

            // Log the response
            Log.d("Retrofit", "Received response from: ${request.url}")
            Log.d("Retrofit", "Response code: ${response.code}")
            Log.d("Retrofit", "Response message: ${response.message}")

            response
        }
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
    }

    val apiService: FlutterwaveApiService by lazy {
        retrofit.create(FlutterwaveApiService::class.java)
    }
}

interface FlutterwaveApiService {
    @Headers(
        "Content-Type: text/plain",
        "User-Agent: PostmanRuntime/7.32.3",
        "Accept: */*",
        "Accept-Encoding: gzip, deflate, br",
        "Connection: keep-alive",
        "Content-Type: application/json",
        "sandbox-key: $API_KEY",
        "Authorization: $AUTHORIZATION"
    )
    @POST("flutterwave/v3/virtual-account-numbers")
    suspend fun createVirtualAccount(
        @Body request: VirtualAccountRequest
    ): VirtualAccountResponse

    @Headers(
        "User-Agent: PostmanRuntime/7.32.3",
        "Accept: */*",
        "Accept-Encoding: gzip, deflate, br",
        "Connection: keep-alive",
        "Content-Type: application/json",
        "sandbox-key: $API_KEY",
        "Authorization: $AUTHORIZATION"
    )
    @GET("flutterwave/v3/virtual-account-numbers/{order_ref}")
    suspend fun getVirtualAccount(
        @Path("order_ref") orderRef: String
    ): VirtualAccountResponse

}
