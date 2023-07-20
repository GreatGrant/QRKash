package com.gralliams.qrkash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.databinding.FragmentDashBoardBinding
import com.gralliams.qrkash.viewmodel.DashboardViewModel
import com.gralliams.qrkash.viewmodel.WalletViewModel


class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var viewModel: DashboardViewModel
    private lateinit var walletViewModel: WalletViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)

        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]

        val username = binding.tvUserName
        val currentUser = FirebaseAuth.getInstance().currentUser
        val displayName = currentUser?.displayName

        if (displayName != null) {
            // Display the user's name
            username.text = displayName
            // Retrieve the user ID
            val userId: String = currentUser.uid
        } else {
            // Handle the case where the display name is not available
            username.text = "User"
        }

        binding.apply {
            btnTopup.setOnClickListener {
                showTopupOptionsDialog()
            }

            btnTransfer.setOnClickListener {
                findNavController().navigate(R.id.action_dashboardFragment_to_qrScanFragment)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]


        walletViewModel.balanceLiveData.observe(viewLifecycleOwner) { balance ->
            binding.tvBalanceAmount.visibility = View.VISIBLE
            binding.tvBalanceAmount.text = "₦${balance}"
        }
    }

    private fun showTopupOptionsDialog() {
        val options = arrayOf("Use QR code", "Use bank transfer")

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Topup Account")
            .setItems(options) { _, index ->
                when (index) {
                    0 -> {
                        findNavController().navigate(R.id.action_dashboardFragment_to_qrGenerateFragment)
                    }
                    1 -> {
                        findNavController().navigate(R.id.action_dashboardFragment_to_virtualAccountFragment)
                    }
                }
            }
            .show()
    }
}
