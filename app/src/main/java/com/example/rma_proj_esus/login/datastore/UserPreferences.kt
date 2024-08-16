package com.example.rma_proj_esus.login.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Define the DataStore name
private const val USER_PREFERENCES_NAME = "user_preferences"

// Create an extension property for DataStore
val Context.userPreferencesDataStore by preferencesDataStore(name = USER_PREFERENCES_NAME)

// Define the keys for storing login state and credentials
object UserPreferencesKeys {
    val LOGIN_STATE_KEY = booleanPreferencesKey("login_state")
    val EMAIL_KEY = stringPreferencesKey("email")
}

// Create a data class to hold the user preferences
data class UserPreferences(
    val isLoggedIn: Boolean,
    val email: String?
)
