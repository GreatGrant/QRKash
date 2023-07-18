package com.gralliams.qrkash.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class WalletViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences =
        application.getSharedPreferences("WalletPrefs", Context.MODE_PRIVATE)
    private val walletBalanceKey = "wallet_balance"

    private val _walletBalance = MutableLiveData<Double>()
    val walletBalance: LiveData<Double> get() = _walletBalance

    private val _isBalanceSufficient = MutableLiveData<Boolean>()
    val isBalanceSufficient: LiveData<Boolean> get() = _isBalanceSufficient

    init {
        _walletBalance.value = sharedPreferences.getFloat(walletBalanceKey, 0.0f).toDouble()
        _isBalanceSufficient.value = true // Assume sufficient balance initially
    }

    fun decrementBalance(amount: Double) {
        val currentBalance = _walletBalance.value ?: 0.0
        if (amount <= currentBalance) {
            _walletBalance.value = currentBalance - amount
            saveBalanceToSharedPreferences()
            _isBalanceSufficient.value = true // Sufficient balance
        } else {
            _isBalanceSufficient.value = false // Insufficient balance
        }
    }

    fun incrementBalance(amount: Int) {
        val currentBalance = _walletBalance.value ?: 0.0
        _walletBalance.value = currentBalance + amount
        saveBalanceToSharedPreferences()
        _isBalanceSufficient.value = true // Reset to sufficient balance after incrementing
    }

    private fun saveBalanceToSharedPreferences() {
        sharedPreferences.edit().putFloat(walletBalanceKey, _walletBalance.value!!.toFloat()).apply()
    }
}
