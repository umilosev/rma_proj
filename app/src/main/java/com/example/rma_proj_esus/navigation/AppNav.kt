package com.example.rma_proj_esus.navigation

import LoginScreen
import UserPreferenceRepository
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.rma_proj_esus.cats.details.catsBreedsDetails
import com.example.rma_proj_esus.cats.list.CatsBreedsListScreen
import com.example.rma_proj_esus.cats.list.catBreedsListScreen
import com.example.rma_proj_esus.login.datastore.UserPreferencesKeys.LOGIN_STATE_KEY
import com.example.rma_proj_esus.login.datastore.userPreferencesDataStore
import kotlinx.coroutines.flow.map
import loginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = context.userPreferencesDataStore
    var isLoggedIn by remember { mutableStateOf(false) }

    // Create an instance of UserPreferenceRepository
    val userPreferencesRepository = UserPreferenceRepository(context)

    // Check login state
    LaunchedEffect(Unit) {
        dataStore.data.map { preferences ->
            preferences[LOGIN_STATE_KEY] ?: false
        }.collect { loggedIn ->
            isLoggedIn = loggedIn
        }
    }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {

        loginScreen(
            userPreferencesRepository = userPreferencesRepository, // Pass the repository
            route = "login",
            navController = navController
        )


        catBreedsListScreen(
            route = "cats",
            navController = navController,
            )


        catsBreedsDetails(
            route = "cats/breed/{id}",
            navController = navController,
            )

    }
}
