package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.VoteResponseDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface VoteApi {
    @POST("/api/v1/battles/{battleId}/votes/pre")
    suspend fun submitPreVote(
        @Path("battleId") battleId: String
    ): BaseResponse<VoteResponseDto>

    @POST("/api/v1/battles/{battleId}/votes/post")
    suspend fun submitPost(
        @Path("battleId") battleId: String
    ): BaseResponse<VoteResponseDto>

}