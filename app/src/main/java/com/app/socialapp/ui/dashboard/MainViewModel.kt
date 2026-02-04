package com.app.socialapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.socialapp.data.datastore.UserPreferences
import com.app.socialapp.ui.nav.SemiCircularNavView
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.first

// Data class for UI state
data class MainUiState(
    val isUserLoggedIn: Boolean = false,
    val selectedBottomNavItem: SemiCircularNavView.Item = SemiCircularNavView.Item.FEED
)

class MainViewModel(private val userPreferences: UserPreferences) : ViewModel() {

    // Expose UI state
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState

    init {
        // Load login state from DataStore
        viewModelScope.launch {
            val loggedIn = userPreferences.isLoggedIn.first()
            _uiState.value = _uiState.value.copy(isUserLoggedIn = loggedIn)
        }
    }

    // Update bottom nav selection
    fun setBottomNavItem(item: SemiCircularNavView.Item) {
        _uiState.value = _uiState.value.copy(selectedBottomNavItem = item)
    }

    // Update login state dynamically
    fun updateLoginState(loggedIn: Boolean) {
        _uiState.value = _uiState.value.copy(isUserLoggedIn = loggedIn)
    }
}
