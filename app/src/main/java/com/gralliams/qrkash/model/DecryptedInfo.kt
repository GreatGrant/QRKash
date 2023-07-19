package com.gralliams.qrkash.model

data class DecryptedInfo(
    val recipient: String,
    val amount: String,
    val email: String,
    val account: String,
    val bank: String
)
