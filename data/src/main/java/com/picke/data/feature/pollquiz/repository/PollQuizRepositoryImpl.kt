package com.picke.data.feature.pollquiz.repository

import com.picke.data.common.model.toResult
import com.picke.data.feature.pollquiz.datasource.PollQuizApi
import com.picke.data.feature.pollquiz.model.VoteRequestDto
import com.picke.data.feature.pollquiz.model.toDomainModel
import com.picke.domain.feature.pollquiz.model.PollQuizVoteBoard
import com.picke.domain.feature.pollquiz.repository.PollQuizRepository
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