package com.picke.app.data.repository

import android.util.Log
import com.picke.app.BuildConfig
import com.picke.app.data.local.TokenManager
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

    companion object{
        private const val TAG = "AuthRepositoryImpl_Picke"
    }

    /**
     * 1. 토큰 재발급 수행
     */
    override suspend fun refreshAccessToken(refreshToken: String): Result<Unit> {
        return try {
            if (BuildConfig.DEBUG) Log.d(TAG, "[API_REQ] 토큰 갱신 시도: ${refreshToken.take(8)}...")
            val response = api.refreshAccessToken(refreshToken)

            if (response.statusCode == 200 && response.data != null) {
                val data = response.data
                Log.d(TAG, "[API_RES] 토큰 갱신 성공: ${data.status}")

                tokenManager.saveAccessToken(data.accessToken)
                tokenManager.saveRefreshToken(data.refreshToken)
                Log.d(TAG, "[LOCAL] 새로운 토큰 기기 저장 완료")

                Result.success(Unit)
            } else {
                Log.e(TAG, "[API_RES] 토큰 갱신 실패: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "토큰 갱신 실패"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 토큰 갱신 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }


    /**
     * 2. 소셜 로그인 수행
     */
    override suspend fun login(provider: String, authCode: String, redirectUri: String): Result<AuthBoard> {
        return try {
            val request = SocialLoginRequest(
                authorizationCode = authCode,
                redirectUri = redirectUri
            )
            if (BuildConfig.DEBUG) Log.d(TAG, "[API_REQ] 로그인 시도: ${request}")
            val response = api.login(provider, request)

            if (response.statusCode == 200 && response.data != null) {
                if (BuildConfig.DEBUG) Log.d(TAG, "[API_RES] 로그인 성공: ${response.data}")

                tokenManager.saveAccessToken(response.data.accessToken)
                tokenManager.saveRefreshToken(response.data.refreshToken)
                Log.d(TAG, "[LOCAL] 새로운 토큰 기기 저장 완료")

                val authToken = response.data.toDomain()
                Result.success(authToken)
            } else {
                Log.e(TAG, "[API_RES] 로그인 실패: ${response.statusCode}, 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "로그인 실패"))
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "[API_ERR] 로그인 예외 발생: ${e.message}", e)
            Result.failure(e)
        }
    }


    /**
     * 3. 로그아웃 수행
     */
    override suspend fun logout(): Result<Unit> {
        return try {
            Log.d(TAG, "[API_REQ] 로그아웃 시도")
            val response = api.logout()

            if(response.statusCode == 200 && response.data?.loggedOut == true){
                Log.d(TAG, "[API_RES] 로그아웃 성공: ${response.data}")

                tokenManager.clearAll()
                Result.success(Unit)
            } else{
                Log.e(TAG, "[API_RES] 로그아웃 실패: ${response.error}")
                Result.failure(Exception(response.error?.message ?: "로그아웃 실패"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "[API_ERR] 로그아웃 예외 발생: ${e.message}", e)
            tokenManager.clearAll()
            Result.failure(e)
        }
    }


    /**
     * 4. 회원 탈퇴 수행
     */
    override suspend fun withdraw(reason: String): Result<Unit> {
        return try {
            Log.d(TAG, "[API_REQ] 탈퇴 시도 사유: $reason")
            val request = WithdrawalRequest(reason = reason)
            val response = api.withdraw(request)

            if (response.statusCode == 200 && response.data?.withdrawn == true) {
                Log.d(TAG, "[API_RES] 탈퇴 성공: ${response.data}")

                tokenManager.clearAll()
                Result.success(Unit)
            } else {
                Log.e(TAG, "[API_RES] 탈퇴 실패: ${response.error}")
                Result.failure(Exception(response.error?.message ?: "탈퇴 실패"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "[API_ERR] 탈퇴 예외 발생 ${e.message}", e)
            tokenManager.clearAll()
            Result.failure(e)
        }
    }
}