package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.VoteRequestDto
import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.VoteApi
import com.swyp4.team2.domain.model.VoteStatsBoard
import com.swyp4.team2.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteApi: VoteApi
) : VoteRepository{
    override suspend fun submitPreVote(
        battleId: String,
        optionId: Long
    ): Result<Boolean> {
        return try {
            val response = voteApi.submitPreVote(battleId, VoteRequestDto(optionId))

            if (response.statusCode == 200) {
                Result.success(true)
            } else {
                val errorMessage = response.error?.message ?: "사전 투표에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun submitPostVote(
        battleId: String,
        optionId: Long
    ): Result<Boolean> {
        return try {
            val response = voteApi.submitPostVote(battleId, VoteRequestDto(optionId))

            if (response.statusCode == 200) {
                Result.success(true)
            } else {
                val errorMessage = response.error?.message ?: "사후 투표에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getVoteStats(battleId: String): Result<VoteStatsBoard> {
        return try {
            val response = voteApi.getVoteStats(battleId)
            val responseData = response.data

            if (response.statusCode == 200 && responseData != null) {
                Result.success(responseData.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "투표 통계를 불러오지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}