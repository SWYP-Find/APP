package com.swyp4.team2.domain.model

data class MyBoard(
    val nickname: String,
    val userHandle: String,
    val credit: Int,
    val philosopherInfo: PhilosopherInfo?,
    val hasNewNotice: Boolean
)

data class PhilosopherInfo(
    val name: String,
    val description: String,
    val imageUrl: Any? = null
)