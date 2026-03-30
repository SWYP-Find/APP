package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.AlarmPageDto
import com.swyp4.team2.data.model.BaseResponse
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface AlarmApi {
    // 1. 알림 목록 조회 (페이징 지원)
    @GET("/api/v1/notifications")
    suspend fun getAlarms(
        @Query("category") category: String = "ALL", // ALL, CONTENT, NOTICE, EVENT
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): BaseResponse<AlarmPageDto>

    // 2. 알림 개별 읽음 처리
    @PATCH("/api/v1/notifications/{notificationId}/read")
    suspend fun readAlarm(
        @Path("notificationId") notificationId: Long
    ): BaseResponse<String>

    // 3. 알림 전체 읽음 처리
    @PATCH("/api/v1/notifications/read-all")
    suspend fun readAllAlarms(): BaseResponse<String>
}