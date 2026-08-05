package com.picke.domain.feature.auth.repository

import com.picke.domain.feature.auth.model.AuthBoard

interface AuthRepository {

    suspend fun refreshAccessToken(): Result<Unit>

    suspend fun login(
        provider: String,
        authCode: String,
        redirectUri: String
    ): Result<AuthBoard>

    suspend fun logout(): Result<Unit>

    suspend fun withdraw(reason: String): Result<Unit>
}