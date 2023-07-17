package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
            // Retrieve the user ID
            val userId: String = currentUser.uid
            updateWallet(userId)
        } else {
            // Handle the case where the display name is not available
            username.text = "User"
        }

        binding.btnTopup.setOnClickListener {
            showTopupOptionsDialog()
        }


        return binding.root
    }

    private fun updateWallet(userId: String) {

    }

    private fun showTopupOptionsDialog() {
        val options = arrayOf("Use QR code", "Use bank transfer")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Topup Account")
            .setItems(options) { _, index ->
                when (index) {
                    0 -> {
                        // Handle "Use QR code" option
                        findNavController().navigate(R.id.action_dashboardFragment_to_qrGenerateFragment)
                    }
                    1 -> {
                        // Handle "Use bank transfer" option
                        findNavController().navigate(R.id.action_dashboardFragment_to_virtualAccountFragment)
                    }
                }
            }
            .show()
    }

    

}