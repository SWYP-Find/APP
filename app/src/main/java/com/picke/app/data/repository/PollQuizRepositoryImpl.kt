package com.picke.app.data.repository

import com.picke.app.data.model.VoteRequestDto
import com.picke.app.data.model.toResult
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.PollQuizApi
import com.picke.app.domain.model.PollQuizVoteBoard
import com.picke.app.domain.repository.PollQuizRepository
import javax.inject.Inject

class PollQuizRepositoryImpl @Inject constructor(
    private val pollQuizApi: PollQuizApi
) : PollQuizRepository {
    override suspend fun submitPollVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard> {
        return try {
            pollQuizApi.submitPollVote(battleId, VoteRequestDto(optionId))
                .toResult("투표 제출에 실패했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyPollVote(battleId: Long): Result<PollQuizVoteBoard> {
        return try {
            pollQuizApi.getMyPollVote(battleId)
                .toResult("내 투표 내역을 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitQuizVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard> {
        return try {
            pollQuizApi.submitQuizVote(battleId, VoteRequestDto(optionId))
                .toResult("퀴즈 제출에 실패했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyQuizVote(battleId: Long): Result<PollQuizVoteBoard> {
        return try {
            pollQuizApi.getMyQuizVote(battleId)
                .toResult("내 퀴즈 내역을 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}