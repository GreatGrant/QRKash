package com.gralliams.qrkash.repository

import com.gralliams.qrkash.api.FlutterwaveApiService
import com.gralliams.qrkash.model.VirtualAccountRequest
import com.gralliams.qrkash.model.VirtualAccountResponse

class VirtualAccountRepository(private val apiService: FlutterwaveApiService) {
    suspend fun createVirtualAccount(requestBody: VirtualAccountRequest): VirtualAccountResponse {
        return apiService.createVirtualAccount(requestBody)
    }
}

