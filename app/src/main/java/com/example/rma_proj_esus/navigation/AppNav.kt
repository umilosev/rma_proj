package com.example.rma_proj_esus.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.rma_proj_esus.cats.details.catsBreedsDetails
import com.example.rma_proj_esus.cats.list.catBreedsListScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNav() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "cats",
    ) {
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
