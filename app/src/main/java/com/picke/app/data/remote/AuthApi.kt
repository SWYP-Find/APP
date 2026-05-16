package com.picke.app.data.remote

import com.picke.app.data.model.AuthResponseDto
import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.LogoutResponseDto
import com.picke.app.data.model.SocialLoginRequest
import com.picke.app.data.model.WithdrawalRequest
import com.picke.app.data.model.WithdrawnResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AuthApi{
    /**
     * 1. 토큰 재발급 API
     *
     * @param refreshToken 헤더에 담아 보낼 토큰 문자열
     */
    @POST("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(
        @Header("X-Refresh-Token") refreshToken: String
    ) : BaseResponse<AuthResponseDto>

    /**
     * 2. 소셜 로그인 API
     *
     * @param provider URL Path에 동적으로 들어갈 소셜 제공자
     * @param request 인가 코드와 리다이렉트 URI가 포함된 요청 Body
     */
    @POST("/api/v1/auth/login/{provider}")
    suspend fun login(
        @Path("provider") provider:String,
        @Body request: SocialLoginRequest
    ) : BaseResponse<AuthResponseDto>

    /**
     * 3. 로그아웃 API
     */
    @POST("/api/v1/auth/logout")
    suspend fun logout(): BaseResponse<LogoutResponseDto>

    /**
     * 4. 회원 탈퇴 API
     *
     * @param request 탈퇴 사유가 담긴 요청 Body
     * NOTE: 기본적으로 HTTP 표준에서 DELETE 메서드는 Body를 가지지 않는 것을 권장하지만, 서버 스펙상 탈퇴 사유를 Body로 받아야 하므로 어노테이션을 사용하여 우회 처리함.
     */
    @HTTP(method = "DELETE", path = "/api/v1/me", hasBody = true)
    suspend fun withdraw(
        @Body request: WithdrawalRequest
    ): BaseResponse<WithdrawnResponseDto>
}

