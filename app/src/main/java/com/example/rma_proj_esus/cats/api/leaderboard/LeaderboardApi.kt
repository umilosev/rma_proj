package com.example.rma_proj_esus.cats.api.leaderboard

import com.example.rma_proj_esus.cats.api.leaderboard.model.QuizResultApiModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LeaderboardApi {
    @GET("leaderboard")
    suspend fun getLeaderboard(
        @Query("category") category: Int
    ): Response<List<QuizResultApiModel>>
}
