package com.picke.app.domain.repository

import com.picke.app.domain.model.AuthBoard

interface AuthRepository{
    suspend fun refreshAccessToken(refreshToken: String): Result<Unit>

    suspend fun login(
        provider: String,
        authCode: String,
        redirectUri: String
    ): Result<AuthBoard>

    suspend fun loginKakao(
        provider: String,
        authCode: String,
    ): Result<AuthBoard>

    suspend fun logout(): Result<Unit>
    suspend fun withdraw(): Result<Unit>
}