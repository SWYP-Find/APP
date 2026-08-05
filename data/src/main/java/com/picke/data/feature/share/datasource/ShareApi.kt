package com.picke.data.feature.share.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.mypage.model.MyRecapDto
import com.picke.data.feature.share.model.ShareKeyDto
import com.picke.data.feature.share.model.ShareUrlDto
import retrofit2.http.GET
import retrofit2.http.Path
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

    // 나의 철학자 유형 공유키 가져오기
    @GET("/api/v1/share/recap")
    suspend fun getRecapShareKey(): BaseResponse<ShareKeyDto>

    // 타인의 철학자 유형 정보 가져오기
    @GET("/api/v1/share/recap/{shareKey}")
    suspend fun getRecapDetail(
        @Path("shareKey") shareKey: String
    ): BaseResponse<MyRecapDto>
}