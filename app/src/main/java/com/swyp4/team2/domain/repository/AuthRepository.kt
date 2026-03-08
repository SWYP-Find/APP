package com.swyp4.team2.domain.repository

data class AuthToken(val accessToken: String, val refreshToken: String)

interface AuthRepository{
    suspend fun loginWithKakaoToken(kakaoAccessToken: String): Result<AuthToken>
}