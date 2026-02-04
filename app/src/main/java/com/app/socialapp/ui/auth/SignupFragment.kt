package com.app.socialapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.socialapp.MainActivity
import com.app.socialapp.R
import com.app.socialapp.data.datastore.UserPreferences
import com.app.socialapp.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class SignupFragment : Fragment(R.layout.fragment_sign_up) {

    private lateinit var binding: FragmentSignUpBinding
    private lateinit var userPreferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSignUpBinding.bind(view)
        userPreferences = UserPreferences(requireContext())

        binding.btnSignup.setOnClickListener {
            signupUser()
        }

        binding.ivBack.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigate(
                R.id.action_signupFragment_to_loginFragment
            )
        }
    }

    private fun signupUser() {
        val name = binding.etFullName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validation
        when {
            name.isEmpty() -> {
                binding.etFullName.error = "Name required"
                return
            }
            email.isEmpty() -> {
                binding.etEmail.error = "Email required"
                return
            }
            password.length < 6 -> {
                binding.etPassword.error = "Min 6 characters"
                return
            }
        }

        lifecycleScope.launch {
            // ✅ Save user + auto login
            userPreferences.saveUser(
                name = name,
                email = email,
                password = password
            )

            // ✅ Move to MainActivity
            startActivity(
                Intent(requireContext(), MainActivity::class.java)
            )
            requireActivity().finish()
        }
    }
}
