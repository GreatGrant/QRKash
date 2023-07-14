package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.databinding.FragmentDashBoardBinding

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)
        val username = binding.tvUserName
        val currentUser = FirebaseAuth.getInstance().currentUser
        val displayName = currentUser?.displayName

        if (displayName != null) {
            // Display the user's name
            username.text = displayName
        } else {
            // Handle the case where the display name is not available
            username.text = "User"
        }

        return binding.root
    }

}