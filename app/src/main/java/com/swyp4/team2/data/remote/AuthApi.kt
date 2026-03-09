package com.swyp4.team2.data.remote

import retrofit2.http.Body
import retrofit2.http.POST

data class TokenRefreshRequest(
    val refreshToken: String
)
data class TokenRefreshResponse(
    val accessToken: String,
    val refreshToken: String
)

data class SocialLoginRequest(
    val provider: String,
    val socialToken: String
)
data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
    val userId: Int,
    val nickname: String,
    val characterId: Int?
)

interface AuthApi{
    @POST("/api/auth/refresh")
    suspend fun refreshAccessToken(
        @Body request: TokenRefreshRequest
    ): TokenRefreshResponse

    @POST("/api/v1/auth/kakao")
    suspend fun sendSocialTokenToServer(
        @Body request: SocialLoginRequest
    ): SocialLoginResponse
}

