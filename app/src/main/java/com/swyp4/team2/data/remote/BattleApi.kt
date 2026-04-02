package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.BattleDetailDto
import com.swyp4.team2.data.model.BattleStatusDto
import retrofit2.http.GET
import retrofit2.http.Path

interface BattleApi {
    @GET("/api/v1/battles/{battleId}")
    suspend fun getBattleDetail(
        @Path("battleId") battleId: Long
    ): BaseResponse<BattleDetailDto>

    @GET("/api/v1/battles/{battleId}/status")
    suspend fun getBattleStatus(
        @Path("battleId") battleId: Long
    ): BaseResponse<BattleStatusDto>
}