package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

        sharedViewModel.scannedData.observe(viewLifecycleOwner, { scannedData ->
            binding.apply {

            }
        })
        return binding.root

    }
}