package com.app.socialapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.socialapp.data.datastore.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(
    private val userPreferences: UserPreferences
) : ViewModel() {

    // -----------------------------
    // Login State
    // -----------------------------
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    // -----------------------------
    // Global Loading State
    // -----------------------------
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // -----------------------------
    // Selected Bottom Navigation Item
    // Used for semi circular navigation
    // -----------------------------
    private val _selectedTab = MutableStateFlow(NavTab.FEED)
    val selectedTab: StateFlow<NavTab> = _selectedTab

    init {
        observeLoginState()
    }

    // -----------------------------
    // Observe login state from DataStore
    // -----------------------------
    private fun observeLoginState() {
        viewModelScope.launch {
            userPreferences.isLoggedIn.collect {
                _isLoggedIn.value = it
            }
        }
    }

    // -----------------------------
    // Update Loading State
    // -----------------------------
    fun setLoading(value: Boolean) {
        _isLoading.value = value
    }

    // -----------------------------
    // Update Selected Navigation Tab
    // -----------------------------
    fun selectTab(tab: NavTab) {
        _selectedTab.value = tab
    }

    // -----------------------------
    // Logout
    // -----------------------------
    fun logout() {
        viewModelScope.launch {
            userPreferences.logout()
        }
    }
}

/**
 * Enum representing navigation items
 * Helps with semi circular nav logic
 */
enum class NavTab {
    FEED,
    EXPLORE,
    MAP,
    CAMERA,
    PROFILE
}
