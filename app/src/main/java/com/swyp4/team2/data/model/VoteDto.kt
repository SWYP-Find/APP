package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.VoteStatsBoard
import com.swyp4.team2.domain.model.VoteStatsOptionBoard

data class VoteResponseDto(
    val voteId: String,
    val status: String
)

data class VoteRequestDto(
    val optionId: Long
)

data class VoteStatsDto(
    val options: List<VoteStatsOptionDto>?,
    val totalCount: Int?,
    val updatedAt: String?
)

data class VoteStatsOptionDto(
    val optionId: Long?,
    val label: String?,
    val title: String?,
    val voteCount: Int?,
    val ratio: Float?
)

// DTO를 Domain Model(Board)로 안전하게 변환하는 확장 함수
fun VoteStatsDto.toDomainModel(): VoteStatsBoard {
    return VoteStatsBoard(
        totalCount = this.totalCount ?: 0,
        updatedAt = this.updatedAt ?: "",
        options = this.options?.map { it.toDomainModel() } ?: emptyList()
    )
}

fun VoteStatsOptionDto.toDomainModel(): VoteStatsOptionBoard {
    return VoteStatsOptionBoard(
        optionId = this.optionId.toString() ?: "1",
        label = this.label ?: "",
        title = this.title ?: "",
        voteCount = this.voteCount ?: 0,
        ratio = this.ratio ?: 0f
    )
}