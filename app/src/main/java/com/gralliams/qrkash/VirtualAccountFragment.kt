package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.api.RetrofitClient
import com.gralliams.qrkash.databinding.FragmentVirtualAccountBinding
import com.gralliams.qrkash.repository.VirtualAccountRepository
import com.gralliams.qrkash.viewmodel.VirtualAccountViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class VirtualAccountFragment : Fragment() {
    private lateinit var binding: FragmentVirtualAccountBinding
    private lateinit var viewModel: VirtualAccountViewModel

//    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_virtual_account, container, false)

        // Step 1: Retrieve Firebase User Information
        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val email = user?.email
        val firstName = user?.displayName?.split(" ")?.get(0)
        val lastName = user?.displayName?.split(" ")?.get(1)

        // Step 2: Create the Request Body
        val requestBody = HashMap<String, Any>()
        requestBody["email"] = email ?: ""
        requestBody["is_permanent"] = true
        requestBody["bvn"] = "12345678901"
        requestBody["tx_ref"] = "VA12"
        requestBody["phonenumber"] = ""
        requestBody["firstname"] = firstName ?: ""
        requestBody["lastname"] = lastName ?: ""
        requestBody["narration"] = "$firstName $lastName"

    // Add request body parameters as needed
    viewModel.createVirtualAccount(requestBody)

    // Initialize ViewModel
    val apiService = RetrofitClient.apiService
    val repository = VirtualAccountRepository(apiService)
    viewModel = ViewModelProvider(
        this,
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
    )[VirtualAccountViewModel::class.java]

    // Observe virtualAccountResponse LiveData
    viewModel.virtualAccountResponse.observe(viewLifecycleOwner) { response ->
        // Handle the response in the UI
        // Update the TextView or perform any other UI updates
        response?.let { accountResponse ->
            val note = accountResponse.data.note
            val bankName = accountResponse.data.bankName
            val accountNumber = accountResponse.data.accountNumber
            // Update UI with the retrieved data
        }
    }

    // Observe error LiveData
    viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
        // Handle the error in the UI
        // Show an error message or perform any other error handling
    }



//        // Create a coroutine scope
//        GlobalScope.launch(Dispatchers.IO) {
//            try {
//                val response = apiService.createVirtualAccount(requestBody)
//
//                // Handle the response
//                if (response.isSuccessful) {
//                    val apiResponse = response.body()
//                    // Handle the successful response as needed
//                } else {
//                    // Handle API error
//                }
//            } catch (e: Exception) {
//                // Handle network or other errors
//            }
//        }
        return binding.root
    }

}