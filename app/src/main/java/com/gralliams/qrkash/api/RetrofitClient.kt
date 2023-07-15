package com.gralliams.qrkash.api

import com.gralliams.qrkash.BuildConfig
import com.gralliams.qrkash.model.VirtualAccountResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object RetrofitClient {
    private const val BASE_URL = "https://fsi.ng/api/v1/"
    private const val API_KEY = BuildConfig.API_KEY

    private val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("sandbox-key", API_KEY)
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


interface FlutterwaveApiService {
    @Headers("Content-Type: application/json")
    @POST("flutterwave/v3/virtual-account-numbers")
    suspend fun createVirtualAccount(@Body requestBody: HashMap<String, Any>): VirtualAccountResponse
}