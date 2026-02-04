package com.app.socialapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.socialapp.data.datastore.UserPreferences

class AppViewModelFactory(
    private val userPreferences: UserPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(userPreferences) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
