package com.app.socialapp.data.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        private val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        private val KEY_USER_NAME = stringPreferencesKey("user_name")
        private val KEY_USER_EMAIL = stringPreferencesKey("user_email")
        private val KEY_USER_PASSWORD = stringPreferencesKey("user_password")
    }

    // -------------------------
    // FLOWS
    // -------------------------
    val isLoggedIn: Flow<Boolean> =
        context.dataStore.data.map { it[KEY_IS_LOGGED_IN] ?: false }

    val userEmail: Flow<String?> =
        context.dataStore.data.map { it[KEY_USER_EMAIL] }

    val userPassword: Flow<String?> =
        context.dataStore.data.map { it[KEY_USER_PASSWORD] }

    // -------------------------
    // SIGN UP → SAVE USER
    // -------------------------
    suspend fun saveUser(
        name: String,
        email: String,
        password: String
    ) {
        context.dataStore.edit { prefs ->
            prefs[KEY_USER_NAME] = name
            prefs[KEY_USER_EMAIL] = email
            prefs[KEY_USER_PASSWORD] = password
            prefs[KEY_IS_LOGGED_IN] = true
        }
    }

    // -------------------------
    // LOGIN → VERIFY USER
    // -------------------------
    suspend fun verifyUser(
        email: String,
        password: String
    ): Boolean {
        val prefs = context.dataStore.data.first()

        val savedEmail = prefs[KEY_USER_EMAIL]
        val savedPassword = prefs[KEY_USER_PASSWORD]

        Log.e("","savedEmail:"+savedEmail+",savedPassword:"+savedPassword)
        return if (email == savedEmail && password == savedPassword) {
            context.dataStore.edit {
                it[KEY_IS_LOGGED_IN] = true
            }
            true
        } else {
            false
        }
    }

    // -------------------------
    // LOGOUT
    // -------------------------
    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[KEY_IS_LOGGED_IN] = false
        }
//        context.dataStore.edit { prefs ->
//            prefs.clear()
//        }
    }
}
