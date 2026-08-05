package com.picke.domain.feature.pollquiz.repository

import com.picke.domain.feature.pollquiz.model.PollQuizVoteBoard

interface PollQuizRepository {
    suspend fun submitPollVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard>
    suspend fun getMyPollVote(battleId: Long): Result<PollQuizVoteBoard>

    suspend fun submitQuizVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard>
    suspend fun getMyQuizVote(battleId: Long): Result<PollQuizVoteBoard>
}