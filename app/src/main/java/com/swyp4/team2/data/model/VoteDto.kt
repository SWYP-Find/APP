package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.VoteBoard

data class VoteResponseDto(
    val voteId: String,
    val status: String
)

fun VoteResponseDto.toDomainModel(): VoteBoard {
    return VoteBoard(
        voteId = this.voteId,
        status = this.status
    )
}