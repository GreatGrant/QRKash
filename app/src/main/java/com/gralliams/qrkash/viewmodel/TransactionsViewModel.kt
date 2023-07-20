package com.gralliams.qrkash.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.gralliams.qrkash.db.TransactionItem
import com.gralliams.qrkash.db.TransactionsDao
import com.gralliams.qrkash.db.TransactionsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        // Use coroutines to load transactions from Room database in the background
        viewModelScope.launch(Dispatchers.IO) {
            val transactionsList = transactionsDao.getAllTransactions()
            transactionsList.observeForever { transactions ->
                _transactions.value = transactions
            }
        }
    }

    fun addTransaction(transaction: TransactionItem) {
        // Use coroutines to insert the new transaction into the Room database in the background
        viewModelScope.launch(Dispatchers.IO) {
            transactionsDao.insertTransaction(transaction)
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Remove the observer to avoid potential memory leaks
        transactions.removeObserver { }
    }
}
