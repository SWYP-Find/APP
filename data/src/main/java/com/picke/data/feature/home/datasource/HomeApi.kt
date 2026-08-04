package com.picke.data.feature.home.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.home.model.HomeResponseDto
import retrofit2.http.GET

interface HomeApi {
    @GET("/api/v1/home")
    suspend fun getHomeData(): BaseResponse<HomeResponseDto>
}