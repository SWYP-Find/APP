package com.swyp4.team2.data.repository

import com.swyp4.team2.data.remote.AuthApi
import com.swyp4.team2.data.remote.KakaoLoginRequest
import com.swyp4.team2.domain.repository.AuthRepository
import com.swyp4.team2.domain.repository.AuthToken
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
) : AuthRepository {
    override suspend fun loginWithKakaoToken(kakaoAccessToken: String): Result<AuthToken> {
        return try {
            val response = api.sendKakaoTokenToServer(KakaoLoginRequest(kakaoAccessToken))
            Result.success(AuthToken(response.accessToken, response.refreshToken))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}