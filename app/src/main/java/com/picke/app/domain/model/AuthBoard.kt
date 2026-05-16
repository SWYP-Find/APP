package com.picke.app.domain.model

/**
 * 앱 내부 인증 정보 도메인 모델
 *
 * @property accessToken API 통신 시 헤더에 포함할 엑세스 토큰
 * @property refreshToken 엑세스 토큰 만료 시 재발급을 위한 리프레시 토큰
 * @property userTag 사용자를 식별하는 고유 태그 정보
 * @property isNewUser 신규 유저 여부
 * @property status 유저의 현재 계정 상태
 */
data class AuthBoard(
    val accessToken: String,
    val refreshToken: String,
    val userTag: String,
    val isNewUser: Boolean,
    val status: String
)