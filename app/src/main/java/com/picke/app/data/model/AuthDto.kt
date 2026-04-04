package com.picke.app.data.model

import com.google.gson.annotations.SerializedName
import com.picke.app.domain.model.AuthBoard

// 1. 소셜 로그인 요청 (Request)
data class SocialLoginRequest(
    @SerializedName("authorizationCode")
    val authorizationCode: String,
    @SerializedName("redirectUri")
    val redirectUri: String
)

data class KakaoSocialLoginRequest(
    @SerializedName("authorizationCode")
    val authorizationCode: String,
)

data class AuthResponseDto(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user_tag")
    val userTag: String,
    @SerializedName("status")
    val status: String, // "PENDING" or "ACTIVE"
    @SerializedName("new_user")
    val isNewUser: Boolean
)

data class LogoutResponseDto(
    @SerializedName("logged_out")
    val loggedOut: Boolean,
)

data class WithdrawnResponseDto(
    @SerializedName("withdrawn")
    val withdrawn: Boolean,
)

fun AuthResponseDto.toDomain(): AuthBoard {
    return AuthBoard(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        userTag = this.userTag,
        isNewUser = this.isNewUser,
        status = this.status
    )
}