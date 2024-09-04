package com.example.rma_proj_esus.cats.repository

import android.util.Log
import com.example.rma_proj_esus.cats.api.leaderboard.LeaderboardApi
import com.example.rma_proj_esus.cats.api.leaderboard.model.QuizResultApiModel
import com.example.rma_proj_esus.networking.lbRetrofit
import retrofit2.Response

object LeaderboardRepository {
    private val lbApi : LeaderboardApi = lbRetrofit.create(LeaderboardApi::class.java)

    suspend fun fetchLeaderboardByCategory(categoryId: Int): List<QuizResultApiModel> {
        return try {
            val response = lbApi.getLeaderboard(categoryId)
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("LeaderboardRepository", "Error fetching leaderboard: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("LeaderboardRepository", "Exception while fetching leaderboard", e)
            emptyList()
        }
    }

    suspend fun fetchAllLeaderboards(): List<List<QuizResultApiModel>> {
        val categories = listOf(1, 2, 3) // Assuming these are the available categories
        val results = mutableListOf<List<QuizResultApiModel>>()

        for (category in categories) {
            val leaderboard = fetchLeaderboardByCategory(category)
            if (leaderboard != null) {
                results.add(leaderboard)
            }
        }

        return results
    }
}