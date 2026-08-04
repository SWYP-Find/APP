package com.picke.data.feature.todaybattle.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.todaybattle.model.TodayBattleResponseDto
import retrofit2.http.GET

interface TodayBattleApi {
    @GET("/api/v1/battles/today")
    suspend fun getTodayBattles(): BaseResponse<TodayBattleResponseDto>
}