package com.gralliams.qrkash

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.gralliams.qrkash.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash, container, false)
        val splashImage = binding.splashImage


        // Create an alpha fade animation for the splash image
        val alphaAnimator = ObjectAnimator.ofFloat(splashImage, "alpha", 0f, 1f)
        alphaAnimator.duration = 1000 // Animation duration in milliseconds
        alphaAnimator.interpolator = AccelerateDecelerateInterpolator()

        // Start the animation
        alphaAnimator.start()

        // Use a handler to post a delayed action to open the main activity after the splash screen duration
        Handler(Looper.getMainLooper()).postDelayed({
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }, 3000)

        return binding.root
    }


}