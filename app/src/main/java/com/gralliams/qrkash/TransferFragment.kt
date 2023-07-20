package com.gralliams.qrkash
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.gralliams.qrkash.R
import com.gralliams.qrkash.databinding.BottomSheetLayoutBinding
import com.gralliams.qrkash.databinding.FragmentTransferBinding
import com.gralliams.qrkash.viewmodel.ScannedSharedViewModel
import com.gralliams.qrkash.viewmodel.WalletViewModel

class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var sharedViewModel: ScannedSharedViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
    }

    private fun setupUI() {
//        val scannedData = TransferFragmentArgs.fromBundle(requireArguments()).qrResult
        sharedViewModel = ViewModelProvider(requireActivity())[ScannedSharedViewModel::class.java]
        walletViewModel = ViewModelProvider(requireActivity())[WalletViewModel::class.java]

        sharedViewModel.scannedData.observe(viewLifecycleOwner){scannedText ->
            decryptQR(scannedText)
        }


        binding.apply {
            btnSend.setOnClickListener {
                val isAmountValid = validateField(etAmount, tilAmount, "Enter an amount")
                val isAccountValid = validateField(etAccountNumber, tilAccountNumber, "Enter account number")
                val isRecipientValid = validateField(etUsername, tilUsername, "Enter account number")
                val isBankValid = validateField(etBank, tilBank, "Enter account number")

                if (!isAmountValid || !isRecipientValid || !isBankValid || !isAccountValid) {
                    return@setOnClickListener
                }

                val currentBalance = walletViewModel.balanceLiveData.value ?: 0
                val enteredAmount = etAmount.text.toString().toInt()

                if (enteredAmount > currentBalance) {
                    tilAmount.error = "Insufficient balance: You cannot withdraw more than your balance ₦$currentBalance"
                    return@setOnClickListener
                } else {
                    tilAmount.error = null
                }

                val newBalance = currentBalance - enteredAmount
                walletViewModel.updateBalance(newBalance)

                if (!isNetworkConnected(requireContext())) {
                    // No network connection, show a message or handle it as needed
                    showBottomSheet("No internet connection. Please check your network settings.", R.drawable.baseline_signal_wifi_connected_no_internet_4_24)
                    return@setOnClickListener
                }else{
                    walletViewModel.balanceLiveData.observe(viewLifecycleOwner) { balance ->
                        val message = "Transaction ref: 363378911df712.\nAmount: ${etAmount.text.toString()}\nRecipient: ${etUsername.text.toString()}\nBank: ${etBank.text.toString()}\nAccount: ${etAccountNumber.text.toString()}\nStatus: Success\nBalance: ${balance}"
                        showBottomSheet(message, R.drawable.baseline_security_update_good_24)
                    }
                }



            }
        }
    }

    private fun decryptQR(stringToDecrypt: String?) {
        // Extracting recipient
        val recipientRegex = Regex("Recipient:\\s(.*?),")
        val recipientMatch = stringToDecrypt?.let { recipientRegex.find(it) }
        val recipient = recipientMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting amount
        val amountRegex = Regex("amount:\\s(.*?),")
        val amountMatch = stringToDecrypt?.let { amountRegex.find(it) }
        val amount = amountMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting email
        val emailRegex = Regex("email:\\s(.*?),")
        val emailMatch = stringToDecrypt?.let { emailRegex.find(it) }
        val email = emailMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting account
        val accountRegex = Regex("account:\\s(.*?),")
        val accountMatch = stringToDecrypt?.let { accountRegex.find(it) }
        val account = accountMatch?.groupValues?.get(1)?.trim() ?: ""

        // Extracting bank
        val bankRegex = Regex("bank:\\s(.*)")
        val bankMatch = stringToDecrypt?.let { bankRegex.find(it) }
        val bank = bankMatch?.groupValues?.get(1)?.trim() ?: ""

        // use the extracted information as needed
        binding.apply {
            val currentBalance = walletViewModel.balanceLiveData.value ?: 0
            tvWalletBalance.text = "Balance: ₦$currentBalance"
            etUsername.setText(recipient)
            etAmount.setText(amount)
            etAccountNumber.setText(account)
            etBank.setText(bank)
            etEmail.setText(email)
        }
    }

    private fun isNetworkConnected(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }

    private fun showBottomSheet(message: String, image: Int) {
        val view = BottomSheetLayoutBinding.inflate(layoutInflater)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(view.root)
        Glide.with(this)
            .load(image)
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view.animationView)
        view.messageTextView.text = message
        dialog.setContentView(view.root)
        dialog.show()

        view.closeButton.setOnClickListener {
            // Dismiss the dialog
            dialog.dismiss()
            findNavController().navigate(R.id.action_transferFragment2_to_navigation_home)
        }
    }

    private fun validateField(
        editText: TextInputEditText,
        textInputLayout: TextInputLayout,
        errorMessage: String
    ): Boolean {
        val text = editText.text.toString().trim()
        return if (text.isEmpty()) {
            textInputLayout.error = errorMessage
            false
        } else {
            textInputLayout.error = null
            true
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        if (item.itemId == android.R.id.home) {
//            // Navigate to HomeFragment when the back button is pressed
//            findNavController().navigate(R.id.action_transferFragment2)
//            return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

}
