package com.picke.app.data.model

import com.picke.app.domain.model.*

// [응답 DTO] 투표 제출 성공
data class VoteResponseDto(
    val voteId: Long?,
    val status: String?
)

// [응답 DTO] 투표 통계 조회
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

// [응답 DTO] 내 투표 내역 조회
data class MyVoteResponseDto(
    val battleTitle: String?,
    val preVote: VotedOptionDto?,
    val postVote: VotedOptionDto?,
    val status: String?,
    val opinionChanged: Boolean?
)

data class VotedOptionDto(
    val optionId: Long?,
    val label: String?,
    val title: String?
)

// DTO -> Domain

fun VoteStatsOptionDto.toDomainModel() = VoteStatsOptionBoard(
    optionId = this.optionId ?: 0L,
    label = this.label ?: "",
    title = this.title ?: "",
    voteCount = this.voteCount ?: 0,
    ratio = this.ratio ?: 0f
)

fun VoteStatsDto.toDomainModel() = VoteStatsBoard(
    totalCount = this.totalCount ?: 0,
    updatedAt = this.updatedAt ?: "",
    options = this.options?.map { it.toDomainModel() } ?: emptyList()
)

fun VotedOptionDto.toDomainModel() = VotedOptionBoard(
    optionId = this.optionId ?: 0L,
    label = this.label ?: "",
    title = this.title ?: ""
)

fun MyVoteResponseDto.toDomainModel() = MyVoteBoard(
    battleTitle = this.battleTitle ?: "",
    preVote = this.preVote?.toDomainModel(),
    postVote = this.postVote?.toDomainModel(),
    status = this.status ?: "NONE",
    opinionChanged = this.opinionChanged ?: false
)