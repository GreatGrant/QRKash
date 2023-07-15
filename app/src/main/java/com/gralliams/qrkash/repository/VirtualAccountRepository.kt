package com.gralliams.qrkash.repository

import com.gralliams.qrkash.api.FlutterwaveApiService
import com.gralliams.qrkash.model.VirtualAccountResponse

class VirtualAccountRepository(private val apiService: FlutterwaveApiService) {
    suspend fun createVirtualAccount(requestBody: HashMap<String, Any>): VirtualAccountResponse {
        return apiService.createVirtualAccount(requestBody)
    }
}

