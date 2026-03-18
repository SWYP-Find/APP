package com.swyp4.team2.domain.model

data class AuthToken(
    val accessToken: String,
    val refreshToken: String,
    val userId: Int,
    val isNewUser: Boolean,
    val status: String
)