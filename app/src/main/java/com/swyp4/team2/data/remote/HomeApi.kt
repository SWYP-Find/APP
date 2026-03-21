package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.HomeResponseDto
import retrofit2.http.GET

interface HomeApi {
    @GET("/api/v1/home")
    suspend fun getHomeData(): HomeResponseDto
}