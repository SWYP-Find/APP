package com.picke.app.data.remote

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.picke.app.BuildConfig
import com.picke.app.data.local.TokenManager
import com.picke.app.data.model.AuthResponseDto
import com.picke.app.data.model.BaseResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenAuthenticator @Inject constructor(
    private val tokenManager: TokenManager,
    private val gson: Gson,
    @ApplicationContext private val context: Context
) : Authenticator {

    companion object {
        private const val TAG = "TokenAuthenticator_Picke"
    }

    private val refreshClient: OkHttpClient by lazy { OkHttpClient() }

    @Synchronized
    override fun authenticate(route: Route?, response: Response): Request? {
        if (responseCount(response) >= 2) {
            Log.e(TAG, "[AUTH] 토큰 갱신 후에도 401 지속. 인증 실패 처리.")
            return null
        }

        val refreshToken = tokenManager.getRefreshToken()
        if (refreshToken.isNullOrEmpty()) {
            Log.e(TAG, "[AUTH] 저장된 리프레시 토큰 없음. 재발급 불가.")
            return null
        }

        return try {
            if (BuildConfig.DEBUG) Log.d(TAG, "[AUTH] 401 감지 → 리프레시 토큰으로 재발급 시도")

            val client = refreshClient
            val refreshRequest = Request.Builder()
                .url("${BuildConfig.BASE_URL}/api/v1/auth/refresh")
                .post(ByteArray(0).toRequestBody(null))
                .header("X-Refresh-Token", refreshToken)
                .build()

            val refreshResponse = client.newCall(refreshRequest).execute()
            val body = refreshResponse.body?.string()

            if (refreshResponse.isSuccessful && body != null) {
                val type = object : TypeToken<BaseResponse<AuthResponseDto>>() {}.type
                val parsed: BaseResponse<AuthResponseDto> = gson.fromJson(body, type)
                val data = parsed.data

                if (parsed.statusCode == 200 && data != null) {
                    tokenManager.saveAccessToken(data.accessToken)
                    tokenManager.saveRefreshToken(data.refreshToken)
                    Log.i(TAG, "[AUTH] 토큰 갱신 완료. 원래 요청 재시도.")

                    if (BuildConfig.DEBUG) {
                        Handler(Looper.getMainLooper()).post {
                            Toast.makeText(context, "[DEBUG] 액세스 토큰이 자동 갱신되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }

                    response.request.newBuilder()
                        .header("Authorization", "Bearer ${data.accessToken}")
                        .build()
                } else {
                    Log.e(TAG, "[AUTH] 토큰 갱신 응답 실패: ${parsed.error?.message}")
                    null
                }
            } else {
                Log.e(TAG, "[AUTH] 토큰 갱신 HTTP 실패: ${refreshResponse.code}")
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "[AUTH] 토큰 갱신 중 예외 발생: ${e.message}", e)
            null
        }
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var prior = response.priorResponse
        while (prior != null) {
            count++
            prior = prior.priorResponse
        }
        return count
    }
}