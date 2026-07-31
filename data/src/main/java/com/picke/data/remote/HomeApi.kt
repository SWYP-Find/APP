package com.picke.data.remote

import com.picke.data.model.BaseResponse
import com.picke.data.model.HomeResponseDto
import retrofit2.http.GET

interface HomeApi {
    @GET("/api/v1/home")
    suspend fun getHomeData(): BaseResponse<HomeResponseDto>
}