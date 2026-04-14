package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.ScenarioResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ScenarioApi {
    @GET("/api/v1/battles/{battleId}/scenario")
    suspend fun getScenario(
        @Path("battleId") battleId: String
    ): BaseResponse<ScenarioResponseDto>
}
