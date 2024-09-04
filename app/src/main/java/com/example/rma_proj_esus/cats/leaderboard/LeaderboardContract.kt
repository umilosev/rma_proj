package com.example.rma_proj_esus.cats.leaderboard

import com.example.rma_proj_esus.cats.leaderboard.model.QuizResultUIModel
import com.example.rma_proj_esus.cats.list.CatBreedListContract.CatsBreedsListState.ListError
import com.example.rma_proj_esus.cats.list.CatBreedListContract.CatsListUiEvent

interface LeaderboardContract {
    data class LeaderboardState (
        val category : Int,
        val results : List<QuizResultUIModel> = emptyList(),
        val error: ListError? = null,
        val fetching: Boolean = false,
    ) {
        sealed class ListError {
            data class ListUpdateFailed(val cause: Throwable? = null) : ListError()
        }
    }

    sealed class LeaderboardUiEvent {
        data object Dummy : LeaderboardUiEvent()
    }
}