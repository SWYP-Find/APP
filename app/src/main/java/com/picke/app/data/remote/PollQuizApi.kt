package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.PollQuizVoteResponseDto
import com.picke.app.data.model.VoteRequestDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PollQuizApi {

    // [투표] 선택 제출
    @POST("/api/v1/battles/{battleId}/poll-vote")
    suspend fun submitPollVote(
        @Path("battleId") battleId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<PollQuizVoteResponseDto>

    // [투표] 내 투표 참여 내역 조회
    @GET("/api/v1/battles/{battleId}/poll-vote/me")
    suspend fun getMyPollVote(
        @Path("battleId") battleId: Long
    ): BaseResponse<PollQuizVoteResponseDto>

    // [퀴즈] 선택 제출
    @POST("/api/v1/battles/{battleId}/quiz-vote")
    suspend fun submitQuizVote(
        @Path("battleId") battleId: Long,
        @Body request: VoteRequestDto
    ): BaseResponse<PollQuizVoteResponseDto>

    // [퀴즈] 내 퀴즈 참여 내역 조회
    @GET("/api/v1/battles/{battleId}/quiz-vote/me")
    suspend fun getMyQuizVote(
        @Path("battleId") battleId: Long
    ): BaseResponse<PollQuizVoteResponseDto>
}