package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.local.TokenManager
import com.picke.app.data.model.KakaoSocialLoginRequest
import com.picke.app.data.model.SocialLoginRequest
import com.picke.app.data.model.WithdrawalRequest
import com.picke.app.data.model.toDomain
import com.picke.app.data.remote.AuthApi
import com.picke.app.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton
import com.picke.app.domain.model.AuthBoard

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi,
    private val tokenManager: TokenManager
) : AuthRepository {

    // 토큰 갱신
    override suspend fun refreshAccessToken(refreshToken: String): Result<Unit> {
        return try {
            val response = api.refreshAccessToken(refreshToken)

            if (response.statusCode == 200 && response.data != null) {
                Log.d("AuthRepositoryFlow", "🔄 [토큰 갱신 성공] 원본 응답: ${response.data}")
                Log.d("AuthRepositoryFlow", "🔄 [토큰 갱신 성공] AccessToken: ${response.data.accessToken}")
                Log.d("AuthRepositoryFlow", "🔄 [토큰 갱신 성공] RefreshToken: ${response.data.refreshToken}")

                tokenManager.saveAccessToken(response.data.accessToken)
                tokenManager.saveRefreshToken(response.data.refreshToken)
                Result.success(Unit)
            } else {
                Log.e("AuthRepositoryFlow", "❌ [토큰 갱신 실패] 상태코드: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "토큰 갱신 실패"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryFlow", "❌ [토큰 갱신 예외 발생] ${e.message}")
            Result.failure(e)
        }
    }


    // 소셜 로그인
    override suspend fun login(provider: String, authCode: String, redirectUri: String): Result<AuthBoard> {
        return try {
            val request = SocialLoginRequest(
                authorizationCode = authCode,
                redirectUri = redirectUri
            )
            Log.e("AuthRepositoryFlow", "서버에 보낸 request: ${request}")

            val response = api.login(provider, request)

            if (response.statusCode == 200 && response.data != null) {
                val data = response.data

                Log.d("AuthRepositoryFlow", "🔑 [로그인 성공] 원본 응답: $data")
                Log.d("AuthRepositoryFlow", "🔑 [로그인 성공] AccessToken: ${data.accessToken}")
                Log.d("AuthRepositoryFlow", "🔑 [로그인 성공] RefreshToken: ${data.refreshToken}")

                tokenManager.saveAccessToken(data.accessToken)
                tokenManager.saveRefreshToken(data.refreshToken)

                val authToken = data.toDomain()
                Result.success(authToken)
            } else {
                Log.e("AuthRepositoryFlow", "❌ [로그인 실패] 상태코드: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "로그인 실패"))
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.e("AuthRepositoryFlow", "❌ [로그인 예외 발생] ${e.message}")
            Log.e("AuthRepositoryFlow", "❌ [로그인 예외 발생] 상세내용: ${e.localizedMessage}")
            Result.failure(e)
        }
    }

    override suspend fun loginKakao(
        provider: String,
        authCode: String
    ): Result<AuthBoard> {
        return try {
            val request = KakaoSocialLoginRequest(
                authorizationCode = authCode,
            )
            Log.e("AuthRepositoryFlow", "서버에 보낸 request: ${request}")

            val response = api.loginKakao(provider, request)

            if (response.statusCode == 200 && response.data != null) {
                val data = response.data

                Log.d("AuthRepositoryFlow", "🔑 [로그인 성공] 원본 응답: $data")
                Log.d("AuthRepositoryFlow", "🔑 [로그인 성공] AccessToken: ${data.accessToken}")
                Log.d("AuthRepositoryFlow", "🔑 [로그인 성공] RefreshToken: ${data.refreshToken}")

                tokenManager.saveAccessToken(data.accessToken)
                tokenManager.saveRefreshToken(data.refreshToken)

                val authToken = data.toDomain()
                Result.success(authToken)
            } else {
                Log.e("AuthRepositoryFlow", "❌ [로그인 실패] 상태코드: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "로그인 실패"))
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.e("AuthRepositoryFlow", "❌ [로그인 예외 발생] ${e.message}")
            Log.e("AuthRepositoryFlow", "❌ [로그인 예외 발생] 상세내용: ${e.localizedMessage}")
            Result.failure(e)
        }
    }


    // 로그아웃
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
    override suspend fun withdraw(reason: String): Result<Unit> {
        return try {
            // 사유를 담은 객체 생성
            val request = WithdrawalRequest(reason = reason)

            val response = api.withdraw(request)

            if (response.statusCode == 200 && response.data?.withdrawn == true) {
                Log.d("AuthRepositoryFlow", "✅ [회원 탈퇴 성공]")
                tokenManager.clearAll()
                Result.success(Unit)
            } else {
                Log.e("AuthRepositoryFlow", "❌ [회원 탈퇴 실패] ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "탈퇴 실패"))
            }
        } catch (e: Exception) {
            Log.e("AuthRepositoryFlow", "❌ [회원 탈퇴 예외] ${e.message}")
            // 에러가 나도 토큰은 비워주는 것이 안전할 수 있습니다 (기획에 따라 선택)
            Result.failure(e)
        }
    }
}