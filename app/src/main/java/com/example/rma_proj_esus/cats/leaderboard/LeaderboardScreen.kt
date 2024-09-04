package com.example.rma_proj_esus.cats.leaderboard

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.rma_proj_esus.cats.leaderboard.model.QuizResultUIModel
import com.example.rma_proj_esus.cats.leaderboard.model.UserQuizResultsUiModel
import com.example.rma_proj_esus.cats.list.model.CatBreedListUiModel
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

    val processedResults = processResults(lbState.results)

    QuizResultList(
        processedResults,
        paddingValues = PaddingValues(4.dp)
    )
}

fun processResults(results: List<QuizResultUIModel>): Map<String, UserQuizResultsUiModel> {
    val resultMap = mutableMapOf<String, UserQuizResultsUiModel>()

    for (result in results) {
        val currentResult = resultMap[result.nickname]

        if (currentResult != null) {
            // Increment the game count
            resultMap[result.nickname] = currentResult.copy(gameCount = currentResult.gameCount + 1)
        } else {
            // Add new entry with the current result as the highest result and game count as 1
            resultMap[result.nickname] = UserQuizResultsUiModel(result.result, 1)
        }
    }

    return resultMap
}

@Composable
fun QuizResultList(
    items: Map<String, UserQuizResultsUiModel>,
    paddingValues: PaddingValues,
) {
    val scrollState = rememberScrollState()
    val intState =

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(paddingValues),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Los categoros: : " + "1"
        )
        Spacer(modifier = Modifier.height(16.dp))

        var ind = 1
        items.forEach {
            Column {
                QuizResultItem(
                    ind++,
                    data = Pair(it.key, it.value)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

}

// count amount of games played by user?
// global id?

@Composable
fun QuizResultItem(
    index: Int,
    data: Pair<String, UserQuizResultsUiModel>,
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        onClick = { Log.d("APP", "Clicked card for user: " + data.first) }
    ) {
        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Index: $index"
        )

        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Nickname: " + data.first
        )

        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Personal best: " + data.second.result
        )

        Text(
            modifier = Modifier.padding(all = 16.dp),
            text = "Game count: " + data.second.gameCount
        )
    }
}