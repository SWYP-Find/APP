package com.swyp4.team2.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class KakaoLoginRequest(val accessToken: String)
data class AuthTokenResponse(val accessToken: String, val refreshToken: String)

interface AuthApi{
    @POST("/api/v1/auth/kakao") // TODO: 추후 수정
    suspend fun sendKakaoTokenToServer(@Body request: KakaoLoginRequest): AuthTokenResponse
}

