package com.picke.data.model

import com.picke.domain.model.PollQuizOptionStatBoard
import com.picke.domain.model.PollQuizVoteBoard

data class VoteRequestDto(
    val optionId: Long
)

data class PollQuizVoteResponseDto(
    val battleId: Long?,
    val selectedOptionId: Long?,
    val totalCount: Int?,
    val stats: List<PollQuizOptionStatDto>?
)

data class PollQuizOptionStatDto(
    val optionId: Long?,
    val title: String?,
    val stance: String?,
    val isCorrect: Boolean?,
    val voteCount: Int?,
    val ratio: Float?
)

fun PollQuizVoteResponseDto.toDomainModel() = PollQuizVoteBoard(
    battleId = this.battleId ?: 0L,
    selectedOptionId = this.selectedOptionId,
    totalCount = this.totalCount ?: 0,
    stats = this.stats?.map { it.toDomainModel() } ?: emptyList()
)

fun PollQuizOptionStatDto.toDomainModel() = PollQuizOptionStatBoard(
    optionId = this.optionId ?: 0L,
    title = this.title ?: "",
    isCorrect = this.isCorrect ?: false,
    stance = this.stance ?: "",
    voteCount = this.voteCount ?: 0,
    ratio = this.ratio ?: 0f
)