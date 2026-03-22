package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.TodayBattleResponseDto
import retrofit2.http.GET

interface TodayBattleApi {
    @GET("/api/v1/battles/today")
    suspend fun getTodayBattles(): BaseResponse<TodayBattleResponseDto>
}