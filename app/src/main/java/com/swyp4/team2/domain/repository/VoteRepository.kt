package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.MyVoteBoard
import com.swyp4.team2.domain.model.VoteStatsBoard


interface VoteRepository {
    suspend fun submitPreVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun submitPostVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun getVoteStats(battleId: Long): Result<VoteStatsBoard>
    suspend fun getMyVoteHistory(battleId: Long): Result<MyVoteBoard>
}