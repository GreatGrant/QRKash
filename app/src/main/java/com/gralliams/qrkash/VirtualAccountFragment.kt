package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.api.RetrofitClient
import com.gralliams.qrkash.databinding.BottomSheetLayoutBinding
import com.gralliams.qrkash.databinding.FragmentVirtualAccountBinding
import com.gralliams.qrkash.model.TransferRequest
import com.gralliams.qrkash.model.TransferResponse
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
    private lateinit var bottomsheetBinding: BottomSheetLayoutBinding
    private lateinit var viewModel: VirtualAccountViewModel
    private lateinit var progressBar: ProgressBar
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_virtual_account, container, false)

        val apiService = RetrofitClient.apiService
        val repository = VirtualAccountRepository(apiService)
        viewModel = ViewModelProvider(
            this,
            VirtualAccountViewModelFactory(repository)
        )[VirtualAccountViewModel::class.java]

        progressBar = binding.progressBar
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
//        viewModel.getVirtualAccount("RND_2641579516055928")

        viewModel.virtualAccountResponse.observe(viewLifecycleOwner) { response ->
            response?.let { accountResponse ->
                progressBar.visibility = View.GONE // Hide the progress bar
                val note = accountResponse.data.note
                val bankName = accountResponse.data.bankName
                val accountNumber = accountResponse.data.accountNumber
                val amount = accountResponse.data.amount
                val orderRef = accountResponse.data.orderRef
                // Extract user's name from note
                val accountName = note.substringAfter("to ")


                // Update UI with the retrieved data
                binding.progressBar.visibility = View.GONE
                binding.apply {
                    tvStatement.text =
                        "${accountResponse.message} $note $bankName $accountNumber \nThis works like a regular bank account number. Transfer from any source to $accountNumber, select $bankName as the destination bank. And funds will be credited to your wallet automatically."
                    tvStatement2.text =
                        " \nSince we are working with a test API, click the button below to simulate a bank transfer to the account number generated with a desired amount."
                    accountNumberEditText.setText(accountNumber)
                    accountNameEditText.setText(accountName)
                    bankEditText.setText(bankName)
                    amountEditText.setText("5500")

                    submitButton.setOnClickListener {
                        it.visibility = View.INVISIBLE
                        val transferRequestBody =
                            TransferRequest(accountBank = bankEditText.text.toString(),
                                accountNumber = accountNumberEditText.text.toString(),
                                amount = amountEditText.text.toString().toInt(),
                                narration = narrationEditText.text.toString(),
                                currency = "NGN",
                                reference = "",
                                callbackUrl = "",
                                debitCurrency = "NGN"
                        )

                        topUpWallet(transferRequestBody)
                    }
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            progressBar.visibility = View.GONE // Hide the progress bar
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun topUpWallet(transferRequest: TransferRequest) {
        progressBar.visibility = View.VISIBLE
        viewModel.createTransfer(transferRequest)
        viewModel.transferResponse.observe(viewLifecycleOwner){response->
            response?.let {
                Toast.makeText(requireContext(), "${response.status} ${response.message}", Toast.LENGTH_SHORT).show()
                progressBar.visibility = View.GONE
                showBottomSheet(it)
            }
        }
    }

    private fun showBottomSheet(response: TransferResponse) {
        val view = BottomSheetLayoutBinding.inflate(layoutInflater)

        view.apply {
         messageTextView.text = "Transaction with ref${response.data.reference} is successful. Check your wallet."
        }
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view.root)
        dialog.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.viewModelScope.cancel()
    }
}
