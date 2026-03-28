package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.AuthBoard

interface AuthRepository{
    suspend fun refreshAccessToken(refreshToken: String): Result<Unit>

    suspend fun login(
        provider: String,
        authCode: String,
        redirectUri: String
    ): Result<AuthBoard>

    suspend fun logout(): Result<Unit>
    suspend fun withdraw(): Result<Unit>
}