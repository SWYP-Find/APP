package com.picke.domain.repository

import com.picke.domain.model.MyVoteBoard
import com.picke.domain.model.VoteStatsBoard

interface VoteRepository {
    suspend fun submitPreVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun submitPostVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun getVoteStats(battleId: Long): Result<VoteStatsBoard>
    suspend fun getMyVoteHistory(battleId: Long): Result<MyVoteBoard>
}