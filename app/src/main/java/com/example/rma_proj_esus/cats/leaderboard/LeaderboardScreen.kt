package com.example.rma_proj_esus.cats.leaderboard

import android.util.Log
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.rma_proj_esus.cats.repository.LeaderboardRepository.fetchLeaderboardByCategory
import kotlinx.coroutines.runBlocking

@ExperimentalMaterial3Api
fun NavGraphBuilder.leaderboardScreen(
    route: String,
    navController: NavController,
) = composable(route = route) {
    LeaderboardScreen()
}

@Composable
fun LeaderboardScreen(
) {
    val lbVm: LeaderboardViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                // We pass the passwordId which we read from arguments above
                return LeaderboardViewModel(category = 1) as T
            }
        }
    )

    val lbState by lbVm.state.collectAsState()

    Log.d("APP", "" + lbState.category + " | " + lbState.results.toString())
}