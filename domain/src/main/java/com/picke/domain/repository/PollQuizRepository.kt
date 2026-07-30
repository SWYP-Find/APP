package com.picke.domain.repository

import com.picke.domain.model.PollQuizVoteBoard

interface PollQuizRepository {
    suspend fun submitPollVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard>
    suspend fun getMyPollVote(battleId: Long): Result<PollQuizVoteBoard>

    suspend fun submitQuizVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard>
    suspend fun getMyQuizVote(battleId: Long): Result<PollQuizVoteBoard>
}