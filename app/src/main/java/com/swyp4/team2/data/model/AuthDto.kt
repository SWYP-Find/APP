package com.swyp4.team2.data.model

// 소셜 로그인
// POST /api/v1/auth/login/{provider}
data class SocialLoginRequest(
    val authorizationCode: String,
    val redirectUri: String
)

data class SocialLoginResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int,
    val isNewUser: Boolean,
    val status: String // "PENDING" or "ACTIVE"
)

// 토큰 갱신
// Access Token을 Refresh Token을 사용하여 재발급
// POST /api/v1/auth/refresh
data class RefreshResponse(
    val accessToken: String,
    val refreshToken: String
)

// 로그아웃
// POST /api/v1/auth/logout
data class LogoutResponse(
    val loggedOut: Boolean
)

// 회원 탈퇴
// DELETE /api/v1/me
data class WithdrawResponse(
    val withdrawn: Boolean
)