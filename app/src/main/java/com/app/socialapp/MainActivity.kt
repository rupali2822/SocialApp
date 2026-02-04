package com.app.socialapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.app.socialapp.data.datastore.UserPreferences
import com.app.socialapp.databinding.ActivityMainBinding
import com.app.socialapp.ui.dashboard.MainViewModelFactory
import com.app.socialapp.ui.main.MainViewModel
import com.app.socialapp.ui.nav.SemiCircularNavView
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var userPreferences: UserPreferences
    private lateinit var viewModel: MainViewModel

    private val authDestinations = setOf(
        R.id.loginFragment,
        R.id.signupFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userPreferences = UserPreferences(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // ----------------- Load login state synchronously -----------------
        val isLoggedIn = runBlocking { userPreferences.isLoggedIn.first() }

        val factory = MainViewModelFactory(userPreferences)
        viewModel = viewModels<MainViewModel> { factory }.value

        // ----------------- Set NavGraph only once -----------------
        if (savedInstanceState == null) {
            val graph = navController.navInflater.inflate(R.navigation.nav_graph)
            graph.setStartDestination(if (isLoggedIn) R.id.feedFragment else R.id.loginFragment)
            navController.graph = graph
        }

        setupBottomNav()
        handleNavVisibility()
        observeUiState()
    }

    // ------------------- Observe ViewModel UI State -------------------
    private fun observeUiState() {
        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { state ->

                // Redirect if user logs in while on login/signup
                if (state.isUserLoggedIn &&
                    navController.currentDestination?.id in authDestinations
                ) {
                    navigateToFeed()
//                    navController.navigate(R.id.feedFragment) {
//                        popUpTo(R.id.loginFragment) { inclusive = true }
//                        launchSingleTop = true
//                    }
                }

                // Update bottom nav visibility
                binding.semiCircularNav.isVisible =
                    state.isUserLoggedIn && navController.currentDestination?.id !in authDestinations

                // Highlight the selected item manually
                binding.semiCircularNav.setSelectedItem(state.selectedBottomNavItem)
            }
        }
    }

    private fun navigateToFeed() {
        val graph = navController.navInflater.inflate(R.navigation.nav_graph)
        graph.setStartDestination(R.id.feedFragment)
        navController.graph = graph
    }

    // ------------------- Bottom Nav -------------------
    private fun setupBottomNav() {
        binding.semiCircularNav.onItemSelected = { item ->
            val destination = when (item) {
                SemiCircularNavView.Item.MAP -> R.id.mapFragment
                SemiCircularNavView.Item.CAMERA -> R.id.cameraFragment
                SemiCircularNavView.Item.FEED -> R.id.feedFragment
                SemiCircularNavView.Item.EXPLORE -> R.id.exploreFragment
                SemiCircularNavView.Item.PROFILE -> R.id.profileFragment
            }

            if (navController.currentDestination?.id != destination &&
                viewModel.uiState.value.isUserLoggedIn
            ) {
                navController.navigate(destination)
                viewModel.setBottomNavItem(item) // Save selection in UI state

                // Update UI immediately

                binding.semiCircularNav.setSelectedItem(item)
            }
        }
    }

    // ------------------- Bottom Nav Visibility -------------------
    private fun handleNavVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.semiCircularNav.isVisible =
                viewModel.uiState.value.isUserLoggedIn && destination.id !in authDestinations
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (!navController.popBackStack()) {
            finish()
        }
    }
}
