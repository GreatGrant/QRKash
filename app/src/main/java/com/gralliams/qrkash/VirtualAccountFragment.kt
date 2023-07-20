package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.api.RetrofitClient
import com.gralliams.qrkash.databinding.BottomSheetLayoutBinding
import com.gralliams.qrkash.databinding.FragmentVirtualAccountBinding
import com.gralliams.qrkash.model.TransferRequest
import com.gralliams.qrkash.model.TransferResponse
import com.gralliams.qrkash.model.VirtualAccountRequest
import com.gralliams.qrkash.repository.VirtualAccountRepository
import com.gralliams.qrkash.viewmodel.ScannedSharedViewModel
import com.gralliams.qrkash.viewmodel.VirtualAccountViewModel
import com.gralliams.qrkash.viewmodel.WalletViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class VirtualAccountFragment : Fragment() {
    private lateinit var binding: FragmentVirtualAccountBinding
    private lateinit var progressBar: ProgressBar
    private val viewModel: VirtualAccountViewModel by lazy {
        ViewModelProvider(
            this,
            VirtualAccountViewModelFactory(VirtualAccountRepository(RetrofitClient.apiService))
        )[VirtualAccountViewModel::class.java]
    }
    private lateinit var walletViewModel: WalletViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        viewModel.virtualAccountResponse.observe(this) { response ->
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
                        "${accountResponse.message}. Please make a transfer to $firstName $lastName $bankName $accountNumber\n\nThis works like a regular bank account number. Transfer from any source to $accountNumber, select $bankName as the destination bank. And funds will be credited to your wallet automatically."
                    tvStatement2.text =
                        " \nSince we are working with a test API, click the button below to simulate a bank transfer to the account number generated with a desired amount."

                    accountNumberEditText.setText(accountNumber)
                    bankEditText.setText(bankName)
                    accountNameEditText.setText("$firstName $lastName")
                    submitButton.setOnClickListener {

                        val isAmountValid =
                            validateField(amountEditText, amountField, "Field cannot be blank.")
                        val isAccountNameValid = validateField(
                            accountNameEditText,
                            accountNameField,
                            "Field cannot be blank."
                        )
                        val isBankValid =
                            validateField(bankEditText, bankField, "Field cannot be blank.")

                        if (!isAmountValid || !isAccountNameValid || !isBankValid) {
                            return@setOnClickListener
                        } else {
                            it.visibility = View.INVISIBLE
                        }

                        // All required fields are filled, proceed with the form submission

                        val transferRequestBody =
                            TransferRequest(
                                accountBank = bankEditText.text.toString(),
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

        viewModel.error.observe(this) { errorMessage ->
            progressBar.visibility = View.GONE // Hide the progress bar
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_virtual_account, container, false)

        walletViewModel = ViewModelProvider(requireActivity())[WalletViewModel::class.java]
        progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE // Show the progress bar

        return binding.root
    }

    private fun topUpWallet(transferRequest: TransferRequest) {
        progressBar.visibility = View.VISIBLE
        viewModel.createTransfer(transferRequest)
        viewModel.transferResponse.observe(this) { response ->
            response?.let {
                sendWebhook(transferRequest)
                progressBar.visibility = View.GONE
                showBottomSheet(it)

            }
        }
    }

    private fun sendWebhook(response: TransferRequest) {
        val currentBalance = walletViewModel.balanceLiveData.value ?: 0
        val newBalance = currentBalance + response.amount
        walletViewModel.updateBalance(newBalance)
    }

    private fun showBottomSheet(response: TransferResponse) {
        val view = BottomSheetLayoutBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view.root)
        dialog.show()

        view.apply {
            messageTextView.text =
                "Transaction with ref${response.data.reference} is successful. Check your wallet."

            closeButton.setOnClickListener {
                // Dismiss the dialog
                dialog.dismiss()
            }
        }
    }

    private fun validateField(
        editText: TextInputEditText,
        textInputLayout: TextInputLayout,
        errorMessage: String
    ): Boolean {
        val text = editText.text.toString().trim()
        if (text.isEmpty()) {
            textInputLayout.error = errorMessage
            return false
        } else {
            textInputLayout.error = null
            return true
        }
    }

}
