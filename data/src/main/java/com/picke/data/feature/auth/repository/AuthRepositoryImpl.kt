package com.picke.data.feature.auth.repository

import com.picke.data.common.local.PreferencesManager
import com.picke.data.feature.auth.model.SocialLoginRequest
import com.picke.data.feature.auth.model.WithdrawalRequest
import com.picke.data.feature.auth.model.toDomain
import com.picke.data.feature.auth.datasource.AuthApi
import com.picke.domain.model.AuthBoard
import com.picke.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val authApi: AuthApi,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun refreshAccessToken(): Result<Unit> {
        return try {
            val refreshToken = preferencesManager.getRefreshToken()
                ?: return Result.failure(Throwable("리프레쉬 토큰 없음"))

            val response = authApi.refreshAccessToken(refreshToken)

            if (response.statusCode == 200 && response.data != null) {
                val data = response.data

                preferencesManager.saveAccessToken(data.accessToken)
                preferencesManager.saveRefreshToken(data.refreshToken)

                Result.success(Unit)
            } else {
                Result.failure(Exception(response.error?.message ?: "토큰 갱신 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(
        provider: String,
        authCode: String,
        redirectUri: String
    ): Result<AuthBoard> {
        return try {
            val request = SocialLoginRequest(
                authorizationCode = authCode,
                redirectUri = redirectUri
            )
            val response = authApi.login(provider, request)

            if (response.statusCode == 200 && response.data != null) {

                preferencesManager.saveAccessToken(response.data.accessToken)
                preferencesManager.saveRefreshToken(response.data.refreshToken)

                val authToken = response.data.toDomain()
                Result.success(authToken)
            } else {
                Result.failure(Exception(response.error?.message ?: "로그인 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = authApi.logout()

            if (response.statusCode == 200 && response.data?.loggedOut == true) {
                preferencesManager.clearAll()
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.error?.message ?: "로그아웃 실패"))
            }
        } catch (e: Exception) {
            preferencesManager.clearAll()
            Result.failure(e)
        }
    }

    override suspend fun withdraw(reason: String): Result<Unit> {
        return try {
            val request = WithdrawalRequest(reason = reason)
            val response = authApi.withdraw(request)

            if (response.statusCode == 200 && response.data?.withdrawn == true) {
                preferencesManager.clearAll()
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.error?.message ?: "탈퇴 실패"))
            }
        } catch (e: Exception) {
            preferencesManager.clearAll()
            Result.failure(e)
        }
    }
}