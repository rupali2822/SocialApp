package com.app.socialapp.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.app.socialapp.MainActivity
import com.app.socialapp.data.datastore.UserPreferences
import com.app.socialapp.databinding.ActivityAuthBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAuthBinding
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        userPreferences = UserPreferences(this)

       // lifecycleScope.launch {
//            if (userPreferences.isLoggedIn.first()) {
//                // 🚀 User already logged in → go directly to Main
//                startActivity(Intent(this@AuthActivity, MainActivity::class.java))
//                finish()
//                return@launch
//            }

            // ❗ Only inflate layout if NOT logged in
            binding = ActivityAuthBinding.inflate(layoutInflater)
            setContentView(binding.root)

    }
}
