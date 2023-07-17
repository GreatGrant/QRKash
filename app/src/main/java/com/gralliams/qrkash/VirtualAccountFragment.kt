package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.api.RetrofitClient
import com.gralliams.qrkash.databinding.FragmentVirtualAccountBinding
import com.gralliams.qrkash.model.VirtualAccountRequest
import com.gralliams.qrkash.repository.VirtualAccountRepository
import com.gralliams.qrkash.viewmodel.VirtualAccountViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class VirtualAccountFragment : Fragment() {
    private lateinit var binding: FragmentVirtualAccountBinding
    private lateinit var viewModel: VirtualAccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_virtual_account, container, false)

        val apiService = RetrofitClient.apiService
        val repository = VirtualAccountRepository(apiService)
        viewModel = ViewModelProvider(this, VirtualAccountViewModelFactory(repository))[VirtualAccountViewModel::class.java]

        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE // Show the progress bar

        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val email = user?.email
        val firstName = user?.displayName?.split(" ")?.get(0)
        val lastName = user?.displayName?.split(" ")?.get(1)

        Toast.makeText(requireContext(), "$email $firstName $lastName", Toast.LENGTH_SHORT).show()

        val requestBody = VirtualAccountRequest(
            email = email ?: "",
            isPermanent = true,
            bvn = "22369861577",
            firstName = firstName ?: "",
            lastName = lastName ?: "",
            narration = "$firstName $lastName"
        )

        viewModel.createVirtualAccount(requestBody)

        viewModel.virtualAccountResponse.observe(viewLifecycleOwner) { response ->
            response?.let { accountResponse ->
                progressBar.visibility = View.GONE // Hide the progress bar
                val note = accountResponse.data.note
                val bankName = accountResponse.data.bankName
                val accountNumber = accountResponse.data.accountNumber
                val orderRef = accountResponse.data.orderRef

                viewModel.getVirtualAccount(orderRef)
                // Update UI with the retrieved data
                binding.textView3.text = "${accountResponse.message} $note $bankName $accountNumber"
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            progressBar.visibility = View.GONE // Hide the progress bar
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.viewModelScope.cancel()
    }
}
