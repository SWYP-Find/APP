package com.picke.app.data.model

import com.google.gson.annotations.SerializedName
import com.picke.app.domain.model.AuthBoard

/**
 *  1&2. 소셜 로그인 및 토큰 재발급 API Response
 *
 * @param isNewUser true일 경우 신규 가입자이므로 온보딩 화면으로 라우팅 해야함
 * @param status 현재 사용자의 상태
 */
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

/**
 * 2. 소셜 로그인 API Request
 */
data class SocialLoginRequest(
    @SerializedName("authorizationCode")
    val authorizationCode: String,
    @SerializedName("redirectUri")
    val redirectUri: String
)

/**
 * 3. 로그아웃 API Response
 */
data class LogoutResponseDto(
    @SerializedName("logged_out")
    val loggedOut: Boolean,
)

/**
 * 4. 회원 탈퇴 API Request
 *
 * @param reason 사용자가 선택한 탈퇴 사유
 */
data class WithdrawalRequest(
    @SerializedName("reason")
    val reason: String
)

/**
 * 4. 회원 탈퇴 API Response
 */
data class WithdrawnResponseDto(
    @SerializedName("withdrawn")
    val withdrawn: Boolean,
)

/**
 * DTO -> Domain Mapper 함수
 */
fun AuthResponseDto.toDomain(): AuthBoard {
    return AuthBoard(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
        userTag = this.userTag,
        isNewUser = this.isNewUser,
        status = this.status
    )
}