package com.picke.app.data.model

import com.google.gson.annotations.SerializedName
import com.picke.app.domain.model.AuthBoard

// 소셜 로그인
data class SocialLoginRequest(
    @SerializedName("authorizationCode")
    val authorizationCode: String,
    @SerializedName("redirectUri")
    val redirectUri: String
)

// 소셜 로그인 & 토큰 재발급
data class AuthResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_tag")
    val userTag: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("new_user")
    val isNewUser: Boolean
)

// 로그아웃
data class LogoutResponseDto(
    @SerializedName("logged_out")
    val loggedOut: Boolean,
)

// 회원 탈퇴
data class WithdrawalRequest(
    @SerializedName("reason")
    val reason: String
)
data class WithdrawnResponseDto(
    @SerializedName("withdrawn")
    val withdrawn: Boolean,
)

// Data -> Domain
fun AuthResponseDto.toDomain(): AuthBoard {
    return AuthBoard(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        userTag = this.userTag,
        isNewUser = this.isNewUser,
        status = this.status
    )
}