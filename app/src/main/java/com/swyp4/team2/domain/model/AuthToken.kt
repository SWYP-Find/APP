package com.swyp4.team2.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val isNewUser: Boolean,
    val userId: Int,
    val nickname: String,
    val characterId: Int?
)