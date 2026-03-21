package com.swyp4.team2.data.model

import com.google.gson.annotations.SerializedName

// 1. 소셜 로그인 요청 (Request)
data class SocialLoginRequest(
    val authorizationCode: String,
    val redirectUri: String
)

// 2. 로그인 & 토큰 갱신 공통 응답 (Response)
data class AuthResponseDto(
    val accessToken: String,
    val refreshToken: String,
    val userTag: String,
    val status: String, // "PENDING" or "ACTIVE"
    @SerializedName("is_new_user")
    val isNewUser: Boolean
)