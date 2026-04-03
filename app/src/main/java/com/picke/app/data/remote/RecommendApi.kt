package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.RecommendationPageResponseDto
import retrofit2.http.GET
import retrofit2.http.Path

interface RecommendApi {
    // 흥미 기반 배틀 추천 조회
    @GET("/api/v1/battles/{battleId}/recommendations/interesting")
    suspend fun getInterestingRecommendations(
        @Path("battleId") battleId: Long
    ): BaseResponse<RecommendationPageResponseDto>
}