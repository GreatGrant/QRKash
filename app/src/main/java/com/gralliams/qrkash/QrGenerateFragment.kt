package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.gralliams.qrkash.api.RetrofitClient
import com.gralliams.qrkash.databinding.FragmentQrGenerateBinding
import com.gralliams.qrkash.repository.VirtualAccountRepository
import com.gralliams.qrkash.viewmodel.QRCodeViewModel
import com.gralliams.qrkash.viewmodel.VirtualAccountViewModel

class QrGenerateFragment : Fragment() {
    private lateinit var binding: FragmentQrGenerateBinding
    private lateinit var viewModel: QRCodeViewModel
    private val vaViewModel: VirtualAccountViewModel by lazy {
        ViewModelProvider(
            this,
            VirtualAccountViewModelFactory(VirtualAccountRepository(RetrofitClient.apiService))
        )[VirtualAccountViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_generate, container, false)
        viewModel = ViewModelProvider(this)[QRCodeViewModel::class.java]

        binding.generateButton.setOnClickListener {
            val amount = binding.etAmount.text.toString()
            val recipient = FirebaseAuth.getInstance().currentUser?.displayName

            val stringToEncrypt = "Recipient: $recipient, amount: $amount, account: 7824822527, email: ${FirebaseAuth.getInstance().currentUser?.email}, bank: WEMA BANK"

            viewModel.generateQRCode(stringToEncrypt)
        }

        viewModel.qrCodeImage.observe(viewLifecycleOwner, Observer { bitmap ->
            Glide.with(requireContext())
                .load(bitmap)
                .into(binding.imageViewInsideCard)
        })

        return binding.root
    }
}
