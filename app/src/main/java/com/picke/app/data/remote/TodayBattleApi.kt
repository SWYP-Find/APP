package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.TodayBattleResponseDto
import retrofit2.http.GET

interface TodayBattleApi {
    @GET("/api/v1/battles/today")
    suspend fun getTodayBattles(): BaseResponse<TodayBattleResponseDto>
}