package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.gralliams.qrkash.adapters.ViewPagerAdapter
import com.gralliams.qrkash.databinding.FragmentWelcomeBinding
import me.relex.circleindicator.CircleIndicator3

class WelcomeFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_welcome, container, false)

        val fragmentList = arrayListOf<Fragment>()
        val adapter = ViewPagerAdapter(
            list = fragmentList,
            lifecycle = lifecycle,
            fragmentManager = requireActivity().supportFragmentManager
        )

        setViewItems(binding, adapter)

        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }

        return binding.root
    }
    private fun setViewItems(view: FragmentWelcomeBinding, adapter: ViewPagerAdapter) {
//        get items from binding
        val viewpager = view.onboardingViewpager
        val indicator: CircleIndicator3 = view.indicator

//        set items
        viewpager.adapter = adapter
        indicator.setViewPager(viewpager)

    }
    }