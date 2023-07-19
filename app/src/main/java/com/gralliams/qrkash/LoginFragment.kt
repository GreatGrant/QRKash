package com.gralliams.qrkash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.gralliams.qrkash.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance()

        val keepLoggedIn = getKeepLoggedInPreference()
        if (keepLoggedIn) {
            // Perform automatic login
            navigateToDashboard()
//            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        }
        binding.btnSignIn.setOnClickListener {
            login()
        }
        binding.tvCreateAccount.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        setupInputValidation()
        return binding.root
    }

    private fun setupInputValidation() {
        binding.etEmail.doOnTextChanged { _, _, _, _ ->
            binding.tilEmail.error = null
        }

        binding.etPassword.doOnTextChanged { _, _, _, _ ->
            binding.tilPassword.error = null
        }
    }

    private fun login() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (!validateEmail(email)) {
            binding.tilEmail.error = "Invalid email address"
            return
        }

        if (password.isEmpty()) {
            binding.tilPassword.error = "Password is required"
            return
        }

        // Authenticate user with Firebase
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val keepLoggedIn = binding.checkboxKeepLoggedIn.isChecked
                    if (keepLoggedIn) {
                        saveKeepLoggedInPreference(true)
                    }
                    navigateToDashboard()
                } else {
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateEmail(email: String): Boolean {
        return if (email.isEmpty()) {
            false
        } else {
            Patterns.EMAIL_ADDRESS.matcher(email).matches()
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


    private fun navigateToDashboard() {
        // Clear the activity stack and start DashboardActivity as a new task
        val intent = Intent(requireContext(), DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}
