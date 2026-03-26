package com.swyp4.team2.data.repository

import com.swyp4.team2.data.local.TokenManager
import com.swyp4.team2.data.model.SocialLoginRequest
import com.swyp4.team2.data.remote.AuthApi
import com.swyp4.team2.domain.model.AuthToken
import com.swyp4.team2.domain.repository.AuthRepository
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    // 토큰 갱신
    override suspend fun refreshAccessToken(refreshToken: String): Result<Unit> {
        delay(500L)

        // 무조건 성공한다고 가정! (실패하는 경우를 테스트하고 싶으면 Result.failure(Exception()) 으로 바꾸면 됩니다)
        tokenManager.saveAccessToken("dummy_new_access_token")
        tokenManager.saveRefreshToken("dummy_new_refresh_token")

        return Result.success(Unit)
    }

    /*override suspend fun refreshAccessToken(refreshToken: String): Result<Unit> {
        return try {
            // 1. 진짜 서버에 토큰 갱신 요청을 보냅니다.
            val response = api.refreshAccessToken(refreshToken)

            // 2. 서버가 200(성공)을 주고 데이터가 잘 왔는지 확인합니다.
            if (response.statusCode == 200 && response.data != null) {
                tokenManager.saveAccessToken(response.data.accessToken)
                tokenManager.saveRefreshToken(response.data.refreshToken)
                Result.success(Unit)
            } else {
                // 서버가 에러 메시지를 보냈을 경우 (예: "만료된 토큰입니다")
                Result.failure(Exception(response.error?.message ?: "토큰 갱신 실패"))
            }
        } catch (e: Exception) {
            // 인터넷이 끊겼거나 서버가 터졌을 때
            Result.failure(e)
        }
    }*/


    // 소셜 로그인
    override suspend fun login(provider: String, authCode: String, redirectUri: String): Result<AuthToken> {
        delay(500L)

        val dummyToken = AuthToken(
            accessToken = "dummy_access_token",
            refreshToken = "dummy_refresh_token",
            userTag = "1",
            isNewUser = true,
            status = "ACTIVE"
        )

        // 로컬에 저장
        tokenManager.saveAccessToken(dummyToken.accessToken)
        tokenManager.saveRefreshToken(dummyToken.refreshToken)

        return Result.success(dummyToken)
    }

    /*override suspend fun login(provider: String, authCode: String, redirectUri: String): Result<AuthToken> {
        return try {
            // 1. 서버로 보낼 택배 상자(Request DTO)를 포장합니다.
            val request = SocialLoginRequest(
                authorizationCode = authCode,
                redirectUri = redirectUri
            )

            // 2. 진짜 서버에 로그인 요청을 보냅니다.
            val response = api.login(provider, request)

            if (response.statusCode == 200 && response.data != null) {
                val data = response.data

                // 3. 서버가 준 진짜 토큰들을 기기에 저장합니다.
                tokenManager.saveAccessToken(data.accessToken)
                tokenManager.saveRefreshToken(data.refreshToken)

                // 4. UI 쪽에 넘겨줄 데이터(AuthToken)로 예쁘게 가공합니다.
                val authToken = AuthToken(
                    accessToken = data.accessToken,
                    refreshToken = data.refreshToken,
                    userTag = data.userTag,
                    isNewUser = data.isNewUser,
                    status = data.status
                )
                Result.success(authToken)
            } else {
                Result.failure(Exception(response.error?.message ?: "로그인 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }*/


    // 로그아웃
    /*override suspend fun logout(): Result<Unit> {
        delay(300L)
        tokenManager.clearAll()
        return Result.success(Unit)
    }*/
    override suspend fun logout(): Result<Unit> {
        return try {
            api.logout()
            tokenManager.clearAll()
            Result.success(Unit)
        } catch (e: Exception) {
            tokenManager.clearAll()
            Result.failure(e)
        }
    }


    // 회원 탈퇴
    /*override suspend fun withdraw(): Result<Unit> {
        delay(300L)
        tokenManager.clearAll()
        return Result.success(Unit)
    }*/
    override suspend fun withdraw(): Result<Unit> {
        return try {
            api.withdraw()
            tokenManager.clearAll()
            Result.success(Unit)
        } catch (e: Exception) {
            tokenManager.clearAll()
            Result.failure(e)
        }
    }
}