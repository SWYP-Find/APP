package com.swyp4.team2.data.repository

import com.swyp4.team2.data.local.TokenManager
import com.swyp4.team2.data.remote.AuthApi
import com.swyp4.team2.data.remote.SocialLoginRequest
import com.swyp4.team2.data.remote.TokenRefreshRequest
import com.swyp4.team2.domain.repository.AuthRepository
import com.swyp4.team2.domain.repository.AuthToken
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    override suspend fun refreshAccessToken(refreshToken: String): Result<Unit> {
        return try {
            val request = TokenRefreshRequest(refreshToken)
            val response = api.refreshAccessToken(request)

            // 새로 발급받은 토큰들을 TokenManager를 통해 로컬에 저장!
            tokenManager.saveAccessToken(response.accessToken)
            tokenManager.saveRefreshToken(response.refreshToken)

            Result.success(Unit) // 성공 신호만 UI로 전달
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    override suspend fun loginWithSocialToken(
        provider: String,
        socialToken: String
    ): Result<AuthToken> {
        return try {
            // 1. 통합된 Request 객체 생성
            val request = SocialLoginRequest(
                provider = provider,
                socialToken = socialToken
            )

            // 2. API 호출 (메서드명도 범용적으로 변경)
            val response = api.sendSocialTokenToServer(request)

            // 3. 확장된 AuthToken 구조에 맞춰 모든 데이터 매핑
            val authToken = AuthToken(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken,
                isNewUser = response.isNewUser,
                userId = response.userId,
                nickname = response.nickname,
                characterId = response.characterId
            )

            Result.success(authToken)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun withdraw(): Result<Unit> {
        TODO("Not yet implemented")
    }
}