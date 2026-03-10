package com.swyp4.team2.data.remote

import com.swyp4.team2.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// 서버로 향하는 네트워크 요청 헤더에 accessToken을 자동으로 붙여주는 클래스
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestBuilder = originalRequest.newBuilder()

        // 1. 현재 요청하는 API의 경로(Path)를 확인
        val urlPath = originalRequest.url.encodedPath

        // 2. 토큰을 헤더에 붙이면 안 되는 예외 API인지 판별
        val isLoginRequest = urlPath.contains("/api/auth/login")
        val isRefreshRequest = urlPath.contains("/api/auth/refresh")

        // 로컬에 저장된 accessToken 가져오기
        val accessToken = tokenManager.getAccessToken()

        // 3. 토큰이 존재하면서, 동시에 예외 API(로그인, 재발급)가 '아닐 때만' 헤더에 추가!
        if (!accessToken.isNullOrEmpty() && !isLoginRequest && !isRefreshRequest) {
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}