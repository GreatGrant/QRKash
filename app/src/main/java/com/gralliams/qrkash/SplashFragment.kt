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
import com.google.firebase.auth.FirebaseAuth
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
            var auth = FirebaseAuth.getInstance()

            // Check if the user is signed in
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // User is signed in, navigate directly to DashboardActivity
                navigateToDashboard()
            } else {
                // User is not signed in, navigate to WelcomeFragment
                findNavController().navigate(R.id.action_splashFragment_to_welcomeFragment)
            }

        }, 1000)

        return binding.root
    }

    private fun navigateToDashboard() {
        // Clear the activity stack and start DashboardActivity as a new task
        val intent = Intent(requireContext(), DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        requireActivity().finish()
    }


}