package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_virtual_account, container, false)

        val apiService = RetrofitClient.apiService
        val repository = VirtualAccountRepository(apiService)
        viewModel = ViewModelProvider(this, VirtualAccountViewModelFactory(repository))[VirtualAccountViewModel::class.java]


        val firebaseAuth = FirebaseAuth.getInstance()
        val user = firebaseAuth.currentUser
        val email = user?.email
        val firstName = user?.displayName?.split(" ")?.get(0)
        val lastName = user?.displayName?.split(" ")?.get(1)

        val requestBody = HashMap<String, Any>()
        requestBody["email"] = email ?: ""
        requestBody["is_permanent"] = true
        requestBody["bvn"] = "12345678901"
        requestBody["tx_ref"] = ""
        requestBody["phonenumber"] = ""
        requestBody["firstname"] = firstName ?: ""
        requestBody["lastname"] = lastName ?: ""
        requestBody["narration"] = "$firstName $lastName"

        viewModel.createVirtualAccount(requestBody)

        viewModel.virtualAccountResponse.observe(viewLifecycleOwner) { response ->
            response?.let { accountResponse ->
                val note = accountResponse.data.note
                val bankName = accountResponse.data.bankName
                val accountNumber = accountResponse.data.accountNumber
                // Update UI with the retrieved data
                binding.textView3.text = getString(R.string.virtual_account_statement)
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}
