package com.app.socialapp.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.app.socialapp.MainActivity
import com.app.socialapp.R
import com.app.socialapp.data.datastore.UserPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash)

        userPreferences = UserPreferences(this)

        lifecycleScope.launch {
            // Record start time for minimum splash duration
            val startTime = System.currentTimeMillis()

            // Read the login state
            val isLoggedIn = userPreferences.isLoggedIn.first() // suspends until value is loaded

            // Ensure splash shows for at least 1 second
            val elapsed = System.currentTimeMillis() - startTime
            if (elapsed < 1000) {
                kotlinx.coroutines.delay(1000 - elapsed)
            }


            Log.e("","isLoggedIn:"+isLoggedIn)
            // Launch the correct activity
            val nextActivity = if (isLoggedIn) MainActivity::class.java else AuthActivity::class.java
            startActivity(Intent(this@SplashActivity, nextActivity))
            finish()
        }
    }
}