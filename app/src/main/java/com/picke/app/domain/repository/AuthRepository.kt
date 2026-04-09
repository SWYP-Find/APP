package com.picke.app.domain.repository

import com.picke.app.domain.model.AuthBoard

interface AuthRepository{
    // 토큰 재발급
    suspend fun refreshAccessToken(refreshToken: String): Result<Unit>

    // 소셜 로그인
    suspend fun login(
        provider: String,
        authCode: String,
        redirectUri: String
    ): Result<AuthBoard>

    // 로그아웃
    suspend fun logout(): Result<Unit>

    // 탈퇴하기
    suspend fun withdraw(reason: String): Result<Unit>
}