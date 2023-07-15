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
import com.gralliams.qrkash.databinding.FragmentQrGenerateBinding
import com.gralliams.qrkash.viewmodel.QRCodeViewModel

class QrGenerateFragment : Fragment() {
    private lateinit var binding: FragmentQrGenerateBinding
    private lateinit var viewModel: QRCodeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qr_generate, container, false)
        viewModel = ViewModelProvider(this)[QRCodeViewModel::class.java]

        binding.generateButton.setOnClickListener {
            val number = binding.etAmount.text.toString()
            viewModel.generateQRCode(number)
        }

        viewModel.qrCodeImage.observe(viewLifecycleOwner, Observer { bitmap ->
            Glide.with(requireContext())
                .load(bitmap)
                .into(binding.imageViewInsideCard)
        })

        return binding.root
    }
}
