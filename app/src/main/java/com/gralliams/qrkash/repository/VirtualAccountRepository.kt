package com.gralliams.qrkash.repository

import com.gralliams.qrkash.api.FlutterwaveApiService
import com.gralliams.qrkash.model.TransferRequest
import com.gralliams.qrkash.model.TransferResponse
import com.gralliams.qrkash.model.VirtualAccountRequest
import com.gralliams.qrkash.model.VirtualAccountResponse

class VirtualAccountRepository(private val apiService: FlutterwaveApiService) {
    suspend fun createVirtualAccount(requestBody: VirtualAccountRequest): VirtualAccountResponse {
        return apiService.createVirtualAccount(requestBody)
    }

    suspend fun getVirtualAccount(orderRef: String): VirtualAccountResponse{
        return apiService.getVirtualAccount(orderRef, orderRef)
    }

    suspend fun createTransfer(requestBody: TransferRequest): TransferResponse{
        return apiService.createTransfer(requestBody)
    }
}

