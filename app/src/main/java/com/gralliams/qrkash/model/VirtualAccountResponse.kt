package com.gralliams.qrkash.model

import com.google.gson.annotations.SerializedName

data class VirtualAccountResponse(
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

data class VirtualAccountRequest(
    val email: String,
    val isPermanent: Boolean,
    val bvn: String,
    val firstName: String,
    val lastName: String,
    val narration: String
)
//data class representing the request body
data class TransferRequest(
    @SerializedName("account_bank") val accountBank: String,
    @SerializedName("account_number") val accountNumber: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("narration") val narration: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("callback_url") val callbackUrl: String,
    @SerializedName("debit_currency") val debitCurrency: String
)

data class TransferResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TransferData
)

data class TransferData(
    @SerializedName("id") val id: Int,
    @SerializedName("account_number") val accountNumber: String,
    @SerializedName("bank_code") val bankCode: String,
    @SerializedName("full_name") val fullName: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("currency") val currency: String,
    @SerializedName("debit_currency") val debitCurrency: String,
    @SerializedName("amount") val amount: Int,
    @SerializedName("fee") val fee: Int,
    @SerializedName("status") val status: String,
    @SerializedName("reference") val reference: String,
    @SerializedName("meta") val meta: Any?,
    @SerializedName("narration") val narration: String,
    @SerializedName("complete_message") val completeMessage: String,
    @SerializedName("requires_approval") val requiresApproval: Int,
    @SerializedName("is_approved") val isApproved: Int,
    @SerializedName("bank_name") val bankName: String
)
