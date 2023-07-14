package com.gralliams.qrkash

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.firebase.auth.FirebaseAuth
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
//        val email = binding.etEmailAddress.toString()
//        val password = binding.etPassword.toString()
//
//        auth = FirebaseAuth.getInstance()
//        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {task ->
//            if(task.isSuccessful){
//
//            }
//        }
        return binding.root
    }

}