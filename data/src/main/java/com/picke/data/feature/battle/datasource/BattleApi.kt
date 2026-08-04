package com.picke.data.feature.battle.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.battle.model.BattleDetailDto
import com.picke.data.feature.battle.model.BattleStatusDto
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