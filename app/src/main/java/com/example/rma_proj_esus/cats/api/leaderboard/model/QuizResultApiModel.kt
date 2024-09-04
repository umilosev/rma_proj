package com.example.rma_proj_esus.cats.api.leaderboard.model

import kotlinx.serialization.Serializable

@Serializable
data class QuizResultApiModel (
    val category: Int,
    val nickname: String,
    val result: Float,
    val createdAt: Long
)