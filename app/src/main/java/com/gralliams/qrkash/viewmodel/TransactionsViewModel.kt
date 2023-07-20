package com.gralliams.qrkash.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.gralliams.qrkash.db.TransactionItem
import com.gralliams.qrkash.db.TransactionsDao
import com.gralliams.qrkash.db.TransactionsDatabase

class TransactionsViewModel(application: Application) : AndroidViewModel(application) {

    private val _transactions = MutableLiveData<List<TransactionItem>>()
    val transactions: LiveData<List<TransactionItem>>
        get() = _transactions

    private val transactionsDao: TransactionsDao

    init {
        val database = Room.databaseBuilder(
            application,
            TransactionsDatabase::class.java,
            "transactions_database"
        ).build()

        transactionsDao = database.transactionsDao()

        loadTransactions()
    }

    private fun loadTransactions() {
        // Load transactions from Room database
        transactionsDao.getAllTransactions().observeForever { transactions ->
            _transactions.value = transactions
        }
    }

    fun addTransaction(transaction: TransactionItem) {
        // Insert the new transaction into the Room database
        transactionsDao.insertTransaction(transaction)
    }
}
