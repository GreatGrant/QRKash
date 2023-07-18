package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.viewModels
import com.gralliams.qrkash.databinding.FragmentTransferBinding
import com.gralliams.qrkash.viewmodel.ScannedSharedViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [TransferFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TransferFragment : Fragment() {
    private lateinit var binding: FragmentTransferBinding
    private val sharedViewModel: ScannedSharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_transfer, container, false)

        sharedViewModel.scannedData.observe(viewLifecycleOwner) { scannedData ->
            binding.apply {
                decryptQR(scannedData)
            }
        }
        return binding.root

    }

    private fun decryptQR(stringToDecrpt: String?) {

// Define the regex patterns to match the recipient, amount, account, and bank
        val recipientPattern = "Recipient: (.*?),".toRegex()
        val amountPattern = "amount: \\$(.*?),".toRegex()
        val accountPattern = "account: (.*?),".toRegex()
        val bankPattern = "bank: (.*?)$".toRegex()

// Use the find method to extract the information using the regex patterns
        val recipientMatch = stringToDecrpt?.let { recipientPattern.find(it) }
        val amountMatch = stringToDecrpt?.let { amountPattern.find(it) }
        val accountMatch = stringToDecrpt?.let { accountPattern.find(it) }
        val bankMatch = stringToDecrpt?.let { bankPattern.find(it) }

// Extract the matched values
        val recipient = recipientMatch?.groupValues?.getOrNull(1) ?: ""
        val amount = amountMatch?.groupValues?.getOrNull(1) ?: ""
        val account = accountMatch?.groupValues?.getOrNull(1) ?: ""
        val bank = bankMatch?.groupValues?.getOrNull(1) ?: ""

// use the extracted information as needed
        binding.apply {
            etUsername.setText(recipient)
            etAmount.setText(amount)
            etAccountNumber.setText(account)
            //TODO() set bank and email.

            btnSend.setOnClickListener {

            }
        }

    }
}