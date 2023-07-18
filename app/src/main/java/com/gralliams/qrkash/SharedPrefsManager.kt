package com.gralliams.qrkash

import android.content.Context
import android.content.SharedPreferences

class SharedPrefsManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("WalletPrefs", Context.MODE_PRIVATE)

    fun saveBalance(balance: Int) {
        sharedPreferences.edit().putInt("wallet_balance", balance).apply()
    }

    fun getBalance(): Int {
        return sharedPreferences.getInt("wallet_balance", 0)
    }
}
