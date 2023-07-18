package com.gralliams.qrkash.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gralliams.qrkash.App
import com.gralliams.qrkash.SharedPrefsManager

class WalletViewModel : ViewModel() {
    private val sharedPrefsManager = SharedPrefsManager(App.getContext())
    val balanceLiveData = MutableLiveData<Int>()

    init {
        // Load the balance from SharedPreferences when the ViewModel is created
        balanceLiveData.value = sharedPrefsManager.getBalance()
    }

    fun updateBalance(newBalance: Int) {
        balanceLiveData.value = newBalance
        sharedPrefsManager.saveBalance(newBalance)
    }
}
