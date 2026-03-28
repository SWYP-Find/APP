package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.VoteRequestDto
import com.swyp4.team2.data.model.VoteResponseDto
import com.swyp4.team2.data.model.VoteStatsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path



interface VoteApi {
    // 1. 사전 투표 제출
    @POST("/api/v1/battles/{battleId}/votes/pre")
    suspend fun submitPreVote(
        @Path("battleId") battleId: String,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    // 2. 사후 투표 제출
    @POST("/api/v1/battles/{battleId}/votes/post")
    suspend fun submitPostVote(
        @Path("battleId") battleId: String,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    // 투표 통계 조회
    @GET("/api/v1/battles/{battleId}/vote-stats")
    suspend fun getVoteStats(
        @Path("battleId") battleId: String
    ): BaseResponse<VoteStatsDto>
}