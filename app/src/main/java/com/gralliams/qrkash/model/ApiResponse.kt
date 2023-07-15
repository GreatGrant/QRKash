package com.gralliams.qrkash.model

import com.google.gson.annotations.SerializedName

data class ApiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: VirtualAccountData
)

data class VirtualAccountData(
    @SerializedName("response_code") val responseCode: String,
    @SerializedName("response_message") val responseMessage: String,
    @SerializedName("flw_ref") val flwRef: String,
    @SerializedName("order_ref") val orderRef: String,
    @SerializedName("account_number") val accountNumber: String,
    @SerializedName("frequency") val frequency: String,
    @SerializedName("bank_name") val bankName: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("expiry_date") val expiryDate: String,
    @SerializedName("note") val note: String,
    @SerializedName("amount") val amount: Double?
)
