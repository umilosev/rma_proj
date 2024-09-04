package com.example.rma_proj_esus.cats.leaderboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rma_proj_esus.cats.api.leaderboard.model.QuizResultApiModel
import com.example.rma_proj_esus.cats.leaderboard.model.QuizResultUIModel
import com.example.rma_proj_esus.cats.repository.LeaderboardRepository
import com.example.rma_proj_esus.cats.util.longToFormattedDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class LeaderboardViewModel (
    private val category : Int,
    private val repository: LeaderboardRepository = LeaderboardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LeaderboardContract.LeaderboardState(category = category))
    val state = _state.asStateFlow()
    private fun setState(reducer: LeaderboardContract.LeaderboardState.() -> LeaderboardContract.LeaderboardState) =
        _state.getAndUpdate(reducer)

    private val events = MutableSharedFlow<LeaderboardContract.LeaderboardUiEvent>()

    private fun QuizResultApiModel.asQuizResultUiModel() = QuizResultUIModel(
        category = this.category,
        nickname = this.nickname,
        result = this.result,
        createdAt = longToFormattedDate(this.createdAt)
    )

    init {
        fetchLeaderboardDetails()
    }

    private fun fetchLeaderboardDetails() {
        viewModelScope.launch {
            setState { copy(fetching = true) }
            try {
                val quizResults: List<QuizResultUIModel> = withContext(Dispatchers.IO) {
                    repository.fetchLeaderboardByCategory(category).map { it.asQuizResultUiModel() }
                }
                setState {
                    copy(results = quizResults)
                }
            } catch (error: IOException) {
                setState {
                    copy(
                        error = LeaderboardContract.LeaderboardState.ListError.ListUpdateFailed(
                            cause = error
                        )
                    )
                }
            } finally {
                setState { copy(fetching = false) }
            }
        }
    }
}