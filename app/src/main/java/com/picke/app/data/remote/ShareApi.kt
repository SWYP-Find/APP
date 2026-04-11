package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.ShareUrlDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ShareApi {
    // 철학자 유형 공유하기
    @GET("/api/v1/share/report")
    suspend fun getReportShareLink(
        @Query("reportId") reportId: Int
    ): BaseResponse<ShareUrlDto>

    // 배틀 유형 공유하기
    @GET("/api/v1/share/battle")
    suspend fun getBattleShareLink(
        @Query("battleId") battleId: Int
    ): BaseResponse<ShareUrlDto>
}