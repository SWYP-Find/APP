package com.picke.app.data.model

import com.picke.app.domain.model.ProposalBoard

// 서버로 보낼 때 (Request)
data class ProposalRequestDto(
    val category: String,
    val topic: String,
    val positionA: String,
    val positionB: String,
    val description: String
)

// 서버에서 받을 때 (Response)
data class ProposalResponseDto(
    val id: Long?,
    val userId: Long?,
    val nickname: String?,
    val category: String?,
    val topic: String?,
    val positionA: String?,
    val positionB: String?,
    val description: String?,
    val status: String?,
    val createdAt: String?
)

// DTO -> Domain 매퍼
fun ProposalResponseDto.toDomainModel() = ProposalBoard(
    id = this.id ?: 0L,
    userId = this.userId ?: 0L,
    nickname = this.nickname ?: "알 수 없음",
    category = this.category ?: "",
    topic = this.topic ?: "",
    positionA = this.positionA ?: "",
    positionB = this.positionB ?: "",
    description = this.description ?: "",
    status = this.status ?: "PENDING",
    createdAt = this.createdAt?.take(10) ?: ""
)