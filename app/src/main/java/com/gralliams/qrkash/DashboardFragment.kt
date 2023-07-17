package com.gralliams.qrkash
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.gralliams.qrkash.databinding.FragmentDashBoardBinding
import com.gralliams.qrkash.viewmodel.DashboardViewModel

class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashBoardBinding
    private lateinit var viewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_dash_board, container, false)
        viewModel = ViewModelProvider(this)[DashboardViewModel::class.java]


        binding.btnTopup.setOnClickListener {
            showTopupOptionsDialog()
        }

        viewModel.balance.observe(viewLifecycleOwner){balance ->
            binding.tvBalanceAmount.visibility = View.VISIBLE
            binding.tvBalanceAmount.text = "₦$balance"
        }


        return binding.root
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
