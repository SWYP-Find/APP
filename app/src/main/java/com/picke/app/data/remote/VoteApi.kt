package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.MyVoteResponseDto
import com.picke.app.data.model.VoteRequestDto
import com.picke.app.data.model.VoteResponseDto
import com.picke.app.data.model.VoteStatsDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path



interface VoteApi {
    // 1. 사전 투표 제출
    @POST("/api/v1/battles/{battleId}/votes/pre")
    suspend fun submitPreVote(
        @Path("battleId") battleId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    // 2. 사후 투표 제출
    @POST("/api/v1/battles/{battleId}/votes/post")
    suspend fun submitPostVote(
        @Path("battleId") battleId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<VoteResponseDto>

    // 3. 투표 통계 조회
    @GET("/api/v1/battles/{battleId}/vote-stats")
    suspend fun getVoteStats(
        @Path("battleId") battleId: Long
    ): BaseResponse<VoteStatsDto>

    // 4. 내 투표 내역 조회
    @GET("/api/v1/battles/{battleId}/votes/me")
    suspend fun getMyVoteHistory(
        @Path("battleId") battleId: Long
    ): BaseResponse<MyVoteResponseDto>
}