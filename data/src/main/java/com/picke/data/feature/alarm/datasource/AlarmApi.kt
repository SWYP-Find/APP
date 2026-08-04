package com.picke.data.feature.alarm.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.alarm.model.AlarmDetailDto
import com.picke.data.feature.alarm.model.AlarmPageDto
import com.picke.data.feature.alarm.model.AlarmUnreadDto
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

    // 미읽음 알림 존재 여부 조회 (벨 아이콘 배지용)
    @GET("/api/v1/notifications/unread")
    suspend fun getUnreadExists(
        @Query("category") category: String? = null // ALL, CONTENT, NOTICE, EVENT (생략 시 전체 기준)
    ): BaseResponse<AlarmUnreadDto>

    // 알림 상세 조회
    @GET("/api/v1/notifications/{notificationId}")
    suspend fun getAlarmDetail(
        @Path("notificationId") notificationId: Long
    ): BaseResponse<AlarmDetailDto>

    // 2. 알림 개별 읽음 처리
    @PATCH("/api/v1/notifications/{notificationId}/read")
    suspend fun readAlarm(
        @Path("notificationId") notificationId: Long
    ): BaseResponse<String>

    // 3. 알림 전체 읽음 처리
    @PATCH("/api/v1/notifications/read-all")
    suspend fun readAllAlarms(): BaseResponse<AlarmUnreadDto>
}