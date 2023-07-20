package com.gralliams.qrkash.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.R
import com.gralliams.qrkash.TransactionsViewModelFactory
import com.gralliams.qrkash.adapters.TransactionsAdapter
import com.gralliams.qrkash.databinding.FragmentHomeBinding
import com.gralliams.qrkash.db.TransactionItem
import com.gralliams.qrkash.viewmodel.DashboardViewModel
import com.gralliams.qrkash.viewmodel.TransactionsViewModel
import com.gralliams.qrkash.viewmodel.WalletViewModel

class HomeFragment : Fragment() {
//    private lateinit var transactionsViewModel: TransactionsViewModel
    private var _binding: FragmentHomeBinding? = null
  // This property is only valid between onCreateView and
  // onDestroyView.

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 101
    }private val binding get() = _binding!!
    private lateinit var viewModel: DashboardViewModel
    private lateinit var walletViewModel: WalletViewModel
    private lateinit var transactionsAdapter: TransactionsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]
//todo() fix this bug
//        val application = requireActivity().application // or requireContext().applicationContext if you prefer
//        val viewModelFactory = TransactionsViewModelFactory(application)
//        transactionsViewModel = ViewModelProvider(this, viewModelFactory)[TransactionsViewModel::class.java]



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
                // Check if the camera permission is already granted
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Camera permission is granted, proceed with your app's flow
                    navigateToQrScanFragment()
                } else {
                    // Camera permission is not granted, request it
                    requestCameraPermission()
                }
            }

            ivAvatar.setOnClickListener {
                findNavController().navigate(R.id.action_navigation_home_to_navigation_profile)
            }

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletViewModel = ViewModelProvider(this)[WalletViewModel::class.java]
        observeWalletBalance()

        // Initialize the ListView adapter with an empty list
        transactionsAdapter = TransactionsAdapter(requireContext(), emptyList())
        binding.listViewTransactionHistory.adapter = transactionsAdapter

        //todo() Observe changes in the LiveData and update the ListView
//        transactionsViewModel.transactions.observe(viewLifecycleOwner) { transactions ->
//            transactionsAdapter.clear()
//            transactionsAdapter.addAll(transactions)
//        }


    }

    private fun observeWalletBalance() {
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
                        findNavController().navigate(R.id.action_navigation_home_to_qrGenerateFragment2)
                    }
                    1 -> {
                        findNavController().navigate(R.id.action_navigation_home_to_virtualAccountFragment2)
                    }
                }
            }
            .show()
    }

    private fun requestCameraPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            CAMERA_PERMISSION_REQUEST_CODE
        )
    }

    override fun onResume() {
        super.onResume()
        observeWalletBalance()
    }
    override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission is granted, proceed with your app's flow
                // For example, navigate to the QrScanFragment
                navigateToQrScanFragment()
            } else {
                // Camera permission is denied, handle this case (e.g., show an explanation or exit the app)
                // You can show a toast or dialog explaining why the permission is required
                Toast.makeText(
                    requireContext(),
                    "Camera permission is required to use the QR scanner.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun navigateToQrScanFragment() {
        findNavController().navigate(R.id.action_navigation_home_to_qrScanFragment2)

    }
}