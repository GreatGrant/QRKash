package com.gralliams.qrkash.api

import com.gralliams.qrkash.BuildConfig
import com.gralliams.qrkash.BuildConfig.API_KEY
import com.gralliams.qrkash.constants.AUTHORIZATION
import com.gralliams.qrkash.constants.BASE_URL
import com.gralliams.qrkash.model.VirtualAccountRequest
import com.gralliams.qrkash.model.VirtualAccountResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
object RetrofitClient {


    private val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "text/plain")
                    .addHeader("Content-Length", "<calculated when request is sent>")
                    .addHeader("Host", "<calculated when request is sent>")
                    .addHeader("User-Agent", "PostmanRuntime/7.32.3")
                    .addHeader("Accept", "*/*")
                    .addHeader("Accept-Encoding", "gzip, deflate, br")
                    .addHeader("Connection", "keep-alive")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("sandbox-key", API_KEY)
                    .addHeader("Authorization", AUTHORIZATION)
                    .build()
                chain.proceed(request)
            }
            .build()


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


//interface FlutterwaveApiService {
//    @Headers(
//        "Content-Type: application/json",
//        "Sandbox-Key: $API_KEY",
//        "Authorization: dskjdks"
//    )    @POST("flutterwave/v3/virtual-account-numbers")
//    suspend fun createVirtualAccount(@Body requestBody: HashMap<String, Any>): VirtualAccountResponse
//}

interface FlutterwaveApiService {
    @Headers(
        "Content-Type: text/plain",
        "Content-Length: <calculated when request is sent>",
        "Host: <calculated when request is sent>",
        "User-Agent: PostmanRuntime/7.32.3",
        "Accept: */*",
        "Accept-Encoding: gzip, deflate, br",
        "Connection: keep-alive",
        "Content-Type: application/json",
        "sandbox-key: $API_KEY}",
        "Authorization: $AUTHORIZATION}"
    )
    @POST("flutterwave/v3/virtual-account-numbers")
    suspend fun createVirtualAccount(
        @Body request: VirtualAccountRequest
    ): VirtualAccountResponse
}
