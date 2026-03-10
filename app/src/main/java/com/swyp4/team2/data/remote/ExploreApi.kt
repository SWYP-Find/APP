package com.swyp4.team2.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

data class ContentResponseDto(
    val id: Int,
    val title: String,
    val description: String,
    val optionA: String,
    val optionB: String,
    val timeLimit: String,
    val participantCount: Int,
    val hashtags: List<String>,
)

interface ExploreApi {
    @GET("/api/v1/dilemmas")
    suspend fun getContents(
        @Query("category") category: String,
        @Query("page") page: Int,
        @Query("size") size: Int=10
    ): List<ContentResponseDto>
}