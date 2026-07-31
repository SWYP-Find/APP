package com.picke.data.remote

import com.picke.data.model.BaseResponse
import com.picke.data.model.TodayBattleResponseDto
import retrofit2.http.GET

interface TodayBattleApi {
    @GET("/api/v1/battles/today")
    suspend fun getTodayBattles(): BaseResponse<TodayBattleResponseDto>
}