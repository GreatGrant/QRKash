package com.gralliams.qrkash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.gralliams.qrkash.repository.VirtualAccountRepository
import com.gralliams.qrkash.viewmodel.VirtualAccountViewModel

class VirtualAccountViewModelFactory(private val repository: VirtualAccountRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VirtualAccountViewModel::class.java)) {
            return VirtualAccountViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
