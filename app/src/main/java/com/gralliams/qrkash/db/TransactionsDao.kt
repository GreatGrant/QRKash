package com.gralliams.qrkash.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionsDao {
    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransactions(): LiveData<List<TransactionItem>>

    @Insert
    fun insertTransaction(transaction: TransactionItem)
}
