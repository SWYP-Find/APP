package com.picke.app.data.repository

import com.picke.app.data.model.VoteRequestDto
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
            val response = pollQuizApi.submitPollVote(battleId, VoteRequestDto(optionId))
            if (response.statusCode == 200 && response.data != null) {
                Result.success(response.data.toDomainModel())
            } else {
                Result.failure(Exception(response.error?.message ?: "투표 제출에 실패했습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyPollVote(battleId: Long): Result<PollQuizVoteBoard> {
        return try {
            val response = pollQuizApi.getMyPollVote(battleId)
            if (response.statusCode == 200 && response.data != null) {
                Result.success(response.data.toDomainModel())
            } else {
                Result.failure(Exception(response.error?.message ?: "내 투표 내역을 불러오지 못했습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitQuizVote(battleId: Long, optionId: Long): Result<PollQuizVoteBoard> {
        return try {
            val response = pollQuizApi.submitQuizVote(battleId, VoteRequestDto(optionId))
            if (response.statusCode == 200 && response.data != null) {
                Result.success(response.data.toDomainModel())
            } else {
                Result.failure(Exception(response.error?.message ?: "퀴즈 제출에 실패했습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyQuizVote(battleId: Long): Result<PollQuizVoteBoard> {
        return try {
            val response = pollQuizApi.getMyQuizVote(battleId)
            if (response.statusCode == 200 && response.data != null) {
                Result.success(response.data.toDomainModel())
            } else {
                Result.failure(Exception(response.error?.message ?: "내 퀴즈 내역을 불러오지 못했습니다."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}