package com.picke.app.data.remote

import android.util.Log
import com.picke.app.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// 서버로 향하는 네트워크 요청 헤더에 accessToken을 자동으로 붙여주는 클래스
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {

    companion object {
        private const val TAG = "Picke_AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. 현재 요청하는 API의 경로(Path)를 확인
        val urlPath = originalRequest.url.encodedPath

        // 2. 토큰을 헤더에 붙이면 안 되는 예외 API인지 판별
        val isLoginRequest = urlPath.contains("/api/v1/auth/login")
        val isRefreshRequest = urlPath.contains("/api/v1/auth/refresh")

        if (isRefreshRequest) {
            Log.w(TAG, "♻️ [토큰 갱신 요청] 서버로 리프레시 API를 쏘고 있습니다! URL: $urlPath")
        }

        // 로컬에 저장된 accessToken 가져오기
        val accessToken = tokenManager.getAccessToken()

        // 3. 토큰이 존재하면서, 동시에 예외 API(로그인, 재발급)가 '아닐 때만' 헤더에 추가!
        if (!accessToken.isNullOrEmpty() && !isLoginRequest && !isRefreshRequest) {
            requestBuilder.addHeader(TAG, "Bearer $accessToken")
        }
        // 4. 서버로 요청을 보내고 응답을 받음
        val response = chain.proceed(requestBuilder.build())

        if (response.code == 401) {
            Log.e(TAG, "🚨 [401 에러] 토큰이 만료되었거나 유효하지 않습니다! URL: $urlPath")
        }
        return response
    }
}