package com.picke.app.domain.repository

import com.picke.app.domain.model.MyVoteBoard
import com.picke.app.domain.model.VoteStatsBoard


interface VoteRepository {
    suspend fun submitPreVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun submitPostVote(battleId: Long, optionId: Long): Result<Boolean>
    suspend fun getVoteStats(battleId: Long): Result<VoteStatsBoard>
    suspend fun getMyVoteHistory(battleId: Long): Result<MyVoteBoard>
}