package com.picke.data.feature.scenario.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.scenario.model.ScenarioResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ScenarioApi {
    @GET("/api/v1/battles/{battleId}/scenario")
    suspend fun getScenario(
        @Path("battleId") battleId: String
    ): BaseResponse<ScenarioResponseDto>
}