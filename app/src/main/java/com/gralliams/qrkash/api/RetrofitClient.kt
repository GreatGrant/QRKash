package com.gralliams.qrkash.api

import com.gralliams.qrkash.model.ApiResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

object RetrofitClient {
    private const val BASE_URL = "https://fsi.ng/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: FlutterwaveApiService by lazy {
        retrofit.create(FlutterwaveApiService::class.java)
    }
}


interface FlutterwaveApiService {
    @Headers("Content-Type: application/json")
    @POST("flutterwave/v3/virtual-account-numbers")
    fun createVirtualAccount(@Body requestBody: HashMap<String, Any>): Call<ApiResponse>
}

