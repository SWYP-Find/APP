package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.VoteBoard
import com.swyp4.team2.domain.model.VoteStatsBoard

interface VoteRepository {
    suspend fun submitPreVote(battleId: String, optionId: Long): Result<Boolean>
    suspend fun submitPostVote(battleId: String, optionId: Long): Result<Boolean>
    suspend fun getVoteStats(battleId: String): Result<VoteStatsBoard>
}