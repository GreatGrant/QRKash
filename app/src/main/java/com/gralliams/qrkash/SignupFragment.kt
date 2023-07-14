package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.gralliams.qrkash.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)

        validateFields()

        return binding.root
    }

    private fun validateFields() {
        val fullName = binding.etFullName.text.toString().trim()
        val email = binding.etEmailAddress.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        val fields = listOf(
            binding.etFullName to fullName,
            binding.etEmailAddress to email,
            binding.etPassword to password,
            binding.etConfirmPassword to confirmPassword
        )

        for ((editText, value) in fields) {
            if (value.isEmpty()) {
                editText.error = "${editText.hint} is required"
                return
            }
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmailAddress.error = "Invalid Email Address"
            return
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords do not match"
            return
        }

        aunthenticateUser(email, password, fullName)
    }

    private fun aunthenticateUser(email: String, password: String, fullName: String) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration success, user account created
                    val user = task.result?.user

                    // Update the user's display name with the full name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { updateProfileTask ->
                            if (updateProfileTask.isSuccessful) {
                                // Profile updated successfully
                                // Proceed with any additional logic or navigation
                                findNavController().navigate(R.id.action_signupFragment_to_dashboardFragment)
                            } else {
                                // Profile update failed
                                val exception = updateProfileTask.exception
                                // Handle the error, display an error message, or take appropriate action
                                Toast.makeText(context, "Failed to create user", Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // Registration failed
                    val exception = task.exception
                    // Handle the error, display an error message, or take appropriate action
                    Toast.makeText(context, "Registration Failed", Toast.LENGTH_SHORT).show()
                }
            }

    }

}