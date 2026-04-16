package com.picke.app.domain.model

data class ProposalBoard(
    val id: Long,
    val userId: Long,
    val nickname: String,
    val category: String,
    val topic: String,
    val positionA: String,
    val positionB: String,
    val description: String,
    val status: String,
    val createdAt: String
)