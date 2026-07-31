package com.picke.data.remote

import com.picke.data.model.BaseResponse
import com.picke.data.model.BattleDetailDto
import com.picke.data.model.BattleStatusDto
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