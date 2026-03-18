package com.swyp4.team2.data.remote

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.RefreshResponse
import com.swyp4.team2.data.model.SocialLoginRequest
import com.swyp4.team2.data.model.SocialLoginResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi{
    // 소셜 로그인
    @POST("/api/v1/auth/login/{provider}")
    suspend fun login(
        @Path("provider") provider:String,
        @Body request: SocialLoginRequest
    ) : BaseResponse<SocialLoginResponse>

    // 토큰 갱신
    @POST("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(
        @Header("X-Refresh-Token") refreshToken: String
    ) : BaseResponse<RefreshResponse>

    // 로그아웃
    @POST("/api/v1/auth/logout")
    suspend fun logout(): BaseResponse<Any>

    // 회원탈퇴
    @DELETE("/api/v1/me")
    suspend fun withdraw(): BaseResponse<Any>
}

