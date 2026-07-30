package com.picke.data.remote

import com.picke.data.model.BaseResponse
import com.picke.data.model.ExplorePageResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ExploreApi {
    // 배틀 검색 및 탐색
    @GET("/api/v1/search/battles")
    suspend fun searchBattles(
        @Query("category") category: String? = null,
        @Query("sort") sort: String = "LATEST",
        @Query("offset") offset: Int? = null,
        @Query("size") size: Int = 10
    ): BaseResponse<ExplorePageResponseDto>
}