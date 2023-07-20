package com.gralliams.qrkash.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.R
import com.gralliams.qrkash.VirtualAccountViewModelFactory
import com.gralliams.qrkash.api.RetrofitClient
import com.gralliams.qrkash.databinding.FragmentProfileBinding
import com.gralliams.qrkash.model.VirtualAccountRequest
import com.gralliams.qrkash.repository.VirtualAccountRepository
import com.gralliams.qrkash.viewmodel.VirtualAccountViewModel

class ProfileFragment : Fragment() {
    private val viewModel: VirtualAccountViewModel by lazy {
        ViewModelProvider(
            this,
            VirtualAccountViewModelFactory(VirtualAccountRepository(RetrofitClient.apiService))
        )[VirtualAccountViewModel::class.java]
    }
    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using DataBindingUtil
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        // Get the current user from Firebase Authentication
        val currentUser = FirebaseAuth.getInstance().currentUser

        // Check if the user is authenticated and not null
        if (currentUser != null) {
            // Retrieve the user's display name and email from Firebase Authentication
            val username = currentUser.displayName
            val email = currentUser.email

            // Set the username and email in the corresponding TextViews
            binding.tvFullName.text = username
            binding.tvEmail.text = email


            viewModel.virtualAccountResponse.observe(requireActivity()) { response ->
                binding.tvBio.text = "Virtual acccout number: ${response.data.accountNumber}, ${response.data.bankName}"
            }

        }

        // Handle button clicks or other actions if needed

        return binding.root
    }
}
