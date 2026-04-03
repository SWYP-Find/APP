package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.ScenarioResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ScenarioApi {
    @GET("/api/v1/battles/{battle_id}/scenario")
    suspend fun getScenario(
        @Path("battle_id") battleId: String
    ): BaseResponse<ScenarioResponseDto>
}
