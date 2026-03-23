package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.VoteBoard

interface VoteRepository {
    suspend fun submitPreVote(battleId: String): Result<VoteBoard>
    suspend fun submitPostVote(battleId: String, voteId: String): Result<VoteBoard>
}