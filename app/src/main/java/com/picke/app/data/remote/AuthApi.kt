package com.picke.app.data.remote

import com.picke.app.data.model.AuthResponseDto
import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.LogoutResponseDto
import com.picke.app.data.model.SocialLoginRequest
import com.picke.app.data.model.WithdrawalRequest
import com.picke.app.data.model.WithdrawnResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi{
    // 토큰 재발급
    @POST("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(
        @Header("X-Refresh-Token") refreshToken: String
    ) : BaseResponse<AuthResponseDto>

    // 소셜 로그인
    @POST("/api/v1/auth/login/{provider}")
    suspend fun login(
        @Path("provider") provider:String,
        @Body request: SocialLoginRequest
    ) : BaseResponse<AuthResponseDto>

    // 로그아웃
    @POST("/api/v1/auth/logout")
    suspend fun logout(): BaseResponse<LogoutResponseDto>

    // 회원 탈퇴
    @HTTP(method = "DELETE", path = "/api/v1/me", hasBody = true)
    suspend fun withdraw(
        @Body request: WithdrawalRequest
    ): BaseResponse<WithdrawnResponseDto>
}

