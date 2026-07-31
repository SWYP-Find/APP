package com.picke.data.remote

import com.picke.data.model.BaseResponse
import com.picke.data.model.ScenarioResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ScenarioApi {
    @GET("/api/v1/battles/{battleId}/scenario")
    suspend fun getScenario(
        @Path("battleId") battleId: String
    ): BaseResponse<ScenarioResponseDto>
}
