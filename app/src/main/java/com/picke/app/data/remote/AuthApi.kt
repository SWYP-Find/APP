package com.picke.app.data.remote

import com.picke.app.data.model.AuthResponseDto
import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.KakaoSocialLoginRequest
import com.picke.app.data.model.LogoutResponseDto
import com.picke.app.data.model.SocialLoginRequest
import com.picke.app.data.model.WithdrawnResponseDto
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
    ) : BaseResponse<AuthResponseDto>

    // 소셜 로그인
    @POST("/api/v1/auth/login/{provider}")
    suspend fun loginKakao(
        @Path("provider") provider:String,
        @Body request: KakaoSocialLoginRequest
    ) : BaseResponse<AuthResponseDto>

    // 토큰 갱신
    @POST("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(
        @Header("X-Refresh-Token") refreshToken: String
    ) : BaseResponse<AuthResponseDto>

    // 로그아웃
    @POST("/api/v1/auth/logout")
    suspend fun logout(): BaseResponse<LogoutResponseDto>

    // 회원탈퇴
    @DELETE("/api/v1/me")
    suspend fun withdraw(): BaseResponse<WithdrawnResponseDto>
}

