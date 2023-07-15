package com.gralliams.qrkash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gralliams.qrkash.model.VirtualAccountResponse
import com.gralliams.qrkash.repository.VirtualAccountRepository
import kotlinx.coroutines.launch

class VirtualAccountViewModel(private val repository: VirtualAccountRepository) : ViewModel() {

    val virtualAccountResponse: MutableLiveData<VirtualAccountResponse> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun createVirtualAccount(requestBody: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.createVirtualAccount(requestBody)
                virtualAccountResponse.value = response
            } catch (e: Exception) {
                error.value = "Network Error"
            }
        }
    }
}
