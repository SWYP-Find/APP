package com.swyp4.team2.data.api

import com.swyp4.team2.data.local.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

// 서버로 향하는 네트워크 요청 헤더에 Access Token을 자동으로 붙여주는 클래스
class AuthInterceptor @Inject constructor(
    private val tokenManager: TokenManager
) : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = tokenManager.getAccessToken()
        val requestBuilder = originalRequest.newBuilder()

        if (!accessToken.isNullOrEmpty()){
            requestBuilder.addHeader("Authorization", "Bearer $accessToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}