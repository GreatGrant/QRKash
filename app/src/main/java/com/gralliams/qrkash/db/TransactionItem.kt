package com.gralliams.qrkash.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionItem(
    val description: String,
    val amount: Double
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}
