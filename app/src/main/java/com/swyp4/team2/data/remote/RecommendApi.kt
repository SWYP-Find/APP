package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.RecommendationPageResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendApi {
    // 흥미 기반 배틀 추천 조회
    @GET("/api/v1/battles/{battleId}/recommendations/interesting")
    suspend fun getInterestingRecommendations(
        @Path("battleId") battleId: Long
    ): BaseResponse<RecommendationPageResponseDto>
}