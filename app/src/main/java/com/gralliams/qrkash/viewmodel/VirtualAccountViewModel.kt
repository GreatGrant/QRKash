package com.gralliams.qrkash.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gralliams.qrkash.model.VirtualAccountResponse
import com.gralliams.qrkash.repository.VirtualAccountRepository
import kotlinx.coroutines.launch
import java.io.IOException

class VirtualAccountViewModel(private val repository: VirtualAccountRepository) : ViewModel() {

    val virtualAccountResponse: MutableLiveData<VirtualAccountResponse> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun createVirtualAccount(requestBody: HashMap<String, Any>) {
        viewModelScope.launch {
            try {
                val response = repository.createVirtualAccount(requestBody)
                virtualAccountResponse.value = response
            } catch (e: IOException) {
                error.value = "Network Error: Please check your internet connection."
            } catch (e: Exception) {
                error.value = "Error: ${e.message}. Please try again later."
            }
        }
    }
}
