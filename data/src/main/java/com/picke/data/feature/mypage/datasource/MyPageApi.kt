package com.picke.data.feature.mypage.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.mypage.model.CreditHistoryPageDto
import com.picke.data.feature.mypage.model.MyBattleRecordPageDto
import com.picke.data.feature.mypage.model.MyContentActivityPageDto
import com.picke.data.feature.mypage.model.MyPageInfoDto
import com.picke.data.feature.mypage.model.MyRecapDto
import com.picke.data.feature.mypage.model.NotificationSettingsDto
import com.picke.data.feature.mypage.model.ProfileUpdateRequestDto
import com.picke.data.feature.mypage.model.ProfileUpdateResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Query

interface MyPageApi {
    // 1. 내 배틀 기록 조회
    @GET("/api/v1/me/battle-records")
    suspend fun getMyBattleRecords(
        @Query("offset") offset: Int? = null,
        @Query("size") size: Int = 10,
        @Query("vote_side") voteSide: String? = null // "PRO" or "CON"
    ): BaseResponse<MyBattleRecordPageDto>

    // 2. 내 활동 조회
    @GET("/api/v1/me/content-activities")
    suspend fun getMyContentActivities(
        @Query("offset") offset: Int? = null,
        @Query("size") size: Int = 10,
        @Query("activity_type") activityType: String? = null // "COMMENT", "LIKE" or "PERSPECTIVE"
    ): BaseResponse<MyContentActivityPageDto>

    // 3. 마이페이지 메인 조회
    @GET("/api/v1/me/mypage")
    suspend fun getMyPageInfo(): BaseResponse<MyPageInfoDto>

    // 4. 알림 설정 조회
    @GET("/api/v1/me/notification-settings")
    suspend fun getNotificationSettings(): BaseResponse<NotificationSettingsDto>

    // 5. 알림 설정 변경
    @PATCH("/api/v1/me/notification-settings")
    suspend fun updateNotificationSettings(
        @Body request: NotificationSettingsDto
    ): BaseResponse<NotificationSettingsDto>

    // 6. 프로필 수정
    @PATCH("/api/v1/me/profile")
    suspend fun updateProfile(
        @Body request: ProfileUpdateRequestDto
    ): BaseResponse<ProfileUpdateResponseDto>

    // 7. 내 결산(Recap) 조회
    @GET("/api/v1/me/recap")
    suspend fun getMyRecap(): BaseResponse<MyRecapDto>

    // 8. 내 크레딧 내역 조회
    @GET("/api/v1/me/credits/history")
    suspend fun getCreditHistory(
        @Query("offset") offset: Int? = null,
        @Query("size") size: Int = 10
    ): BaseResponse<CreditHistoryPageDto>
}