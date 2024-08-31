package com.example.rma_proj_esus.navigation

import LoginScreen
import UserPreferenceRepository
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.rma_proj_esus.cats.details.catsBreedsDetails
import com.example.rma_proj_esus.cats.list.catBreedsListScreen
import com.example.rma_proj_esus.login.datastore.UserPreferencesKeys.LOGIN_STATE_KEY
import com.example.rma_proj_esus.login.datastore.userPreferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import loginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val dataStore = context.userPreferencesDataStore
    var isLoggedIn by remember { mutableStateOf(false) }


    // Check the current route
    // Observe the current route
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

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

    if (currentRoute != "login") {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Text("Gato app", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Nalog") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Leaderboard") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                        }
                    )
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Enciklopedija macaka") },
                        selected = false,
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navController.navigate("cats") {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                            }
                        }
                    )
                    // ...other drawer items
                }
            }
        ) {
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
    } else {
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
}
