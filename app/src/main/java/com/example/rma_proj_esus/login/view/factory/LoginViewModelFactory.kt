package com.example.rma_proj_esus.login.view.factory

import LoginViewModel
import UserPreferenceRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LoginViewModelFactory(
    private val userPreferencesRepository: UserPreferenceRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(userPreferencesRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
