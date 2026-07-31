package com.picke.domain.repository

import com.picke.domain.model.AuthBoard

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