package com.gralliams.qrkash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    // Initialize Firebase Authentication
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        val keepLoggedIn = getKeepLoggedInPreference()
        if (keepLoggedIn) {
            // Perform automatic login
            startActivity(Intent(requireContext(), DashboardActivity::class.java))
//            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        }

        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        auth = FirebaseAuth.getInstance()
        // Set up login button click listener
        binding.btnSignIn.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun login() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()
        val keepLoggedIn = binding.checkboxKeepLoggedIn.isChecked

        // Authenticate user with Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    if (keepLoggedIn) {
                        // Store user's preference to keep them logged in
                        saveKeepLoggedInPreference(true)
                    }
                    // User login successful, handle success case
                    startActivity(Intent(requireContext(), DashboardActivity::class.java))
                } else {
                    // User login failed, handle failure case
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveKeepLoggedInPreference(keepLoggedIn: Boolean) {
        val sharedPref = requireContext().getSharedPreferences("login_pref", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putBoolean("keep_logged_in", keepLoggedIn)
            apply()
        }
    }

    private fun getKeepLoggedInPreference(): Boolean {
        val sharedPref = requireContext().getSharedPreferences("login_pref", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("keep_logged_in", false)
    }
}
