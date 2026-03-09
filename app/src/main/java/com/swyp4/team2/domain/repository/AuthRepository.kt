package com.swyp4.team2.domain.repository

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
    val userId: Int,
    val nickname: String,
    val characterId: Int?
)

interface AuthRepository{
    suspend fun refreshAccessToken(
        refreshToken: String
    ): Result<Unit>

    suspend fun loginWithSocialToken(
        provider: String,
        socialToken: String
    ): Result<AuthToken>

    suspend fun logout(): Result<Unit>
    suspend fun withdraw(): Result<Unit>
}