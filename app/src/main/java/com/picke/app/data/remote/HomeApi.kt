package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.HomeResponseDto
import retrofit2.http.GET

interface HomeApi {
    @GET("/api/v1/home")
    suspend fun getHomeData(): BaseResponse<HomeResponseDto>
}