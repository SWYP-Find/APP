package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.PerspectivePageDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PerspectiveApi {
    @GET("/api/v1/battles/{battleId}/perspectives")
    suspend fun getPerspectives(
        @Path("battleId") battleId: Long,
        @Query("cursor") cursor: String? = null,
        @Query("size") size: Int = 10,
        @Query("optionLabel") optionLabel: String? = null
    ): BaseResponse<PerspectivePageDto>
}