package com.app.socialapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.socialapp.MainActivity
import com.app.socialapp.R
import com.app.socialapp.data.datastore.UserPreferences
import com.app.socialapp.databinding.FragmentLoginBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userPreferences: UserPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentLoginBinding.bind(view)
        userPreferences = UserPreferences(requireContext())

        binding.btnLogin.setOnClickListener {
            loginUser()
        }

        binding.tvSignup.setOnClickListener {
            findNavController().navigate(
                R.id.action_loginFragment_to_signupFragment
            )
        }
    }

    private fun loginUser() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()

        // Validation
        if (email.isEmpty()) {
            binding.etEmail.error = "Email required"
            return
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Password required"
            return
        }

        lifecycleScope.launch {
            val storedEmail = userPreferences.userEmail.first()
            val storedPassword = userPreferences.userPassword.first()

            Log.e("","storedEmail:"+storedEmail+",storedPassword:"+storedPassword)


                val success = userPreferences.verifyUser(email, password)

                if (success) {
                    // ✅ Move to MainActivity
                    startActivity(
                        Intent(requireContext(), MainActivity::class.java)
                    )
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Invalid credentials",
                        Toast.LENGTH_SHORT
                    ).show()
                }

        }
    }
}
