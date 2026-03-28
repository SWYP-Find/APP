package com.swyp4.team2.domain.model

data class AuthBoard(
    val accessToken: String,
    val refreshToken: String,
    val userTag: String,
    val isNewUser: Boolean,
    val status: String
)