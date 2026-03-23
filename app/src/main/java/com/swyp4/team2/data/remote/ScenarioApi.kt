package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.ScenarioResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ScenarioApi {
    @GET("/api/v1/battles/{battle_id}/scenario")
    suspend fun getScenario(
        @Path("battle_id") battleId: String
    ): BaseResponse<ScenarioResponseDto>
}
