package com.gralliams.qrkash.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.R
import com.gralliams.qrkash.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

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
        }

        // Handle button clicks or other actions if needed

        return binding.root
    }
}
