package com.gralliams.qrkash.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ScannedSharedViewModel : ViewModel() {
    private val _scannedData = MutableLiveData<String>()
    val scannedData: LiveData<String> get() = _scannedData

    fun setScannedData(data: String) {
        _scannedData.value = data
    }
}
