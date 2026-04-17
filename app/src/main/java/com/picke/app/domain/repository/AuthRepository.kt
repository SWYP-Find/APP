package com.picke.app.domain.repository

import com.picke.app.domain.model.AuthBoard

interface AuthRepository{
    /**
     * 1. 토큰 재발급 API
     *
     * @param refreshToken 로컬에 저장되어 있던 Refresh Token
     * @return 갱신 성공 시 [Result.success] 반환
     */
    suspend fun refreshAccessToken(refreshToken: String): Result<Unit>

    /**
     * 2. 소셜 로그인 API
     *
     * @param provider 소셜 로그인 제공자 (예: "kakao", "google")
     * @param authCode 소셜 SDK를 통해 발급받은 인가 코드
     * @param redirectUri 소셜 로그인 등에서 요구하는 리다이렉트 URI
     * @return 로그인 성공 시 유저 정보 [AuthBoard] 반환
     */
    suspend fun login(
        provider: String,
        authCode: String,
        redirectUri: String
    ): Result<AuthBoard>

    /**
     * 3. 로그아웃 API
     *
     * @return 로그아웃 성공 시 [Result.success] 반환
     */
    suspend fun logout(): Result<Unit>

    /**
     * 4. 회원 탈퇴 API
     *
     * @param reason 탈퇴 사유 (예: )
     * @return 탈퇴 성공 시 [Result.success] 반환
     */
    suspend fun withdraw(reason: String): Result<Unit>
}