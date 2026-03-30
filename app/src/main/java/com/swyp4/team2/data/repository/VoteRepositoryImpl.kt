package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.VoteRequestDto
import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.VoteApi
import com.swyp4.team2.domain.model.MyVoteBoard
import com.swyp4.team2.domain.model.VoteStatsBoard
import com.swyp4.team2.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteApi: VoteApi
) : VoteRepository{
    override suspend fun submitPreVote(
        battleId: Long,
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
        battleId: Long,
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

    override suspend fun getVoteStats(battleId: Long): Result<VoteStatsBoard> {
        return try {
            val response = voteApi.getVoteStats(battleId)
            val data = response.data ?: throw Exception(response.error?.message ?: "투표 통계를 불러오지 못했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyVoteHistory(battleId: Long): Result<MyVoteBoard> {
        return try {
            val response = voteApi.getMyVoteHistory(battleId)
            val data = response.data ?: throw Exception(response.error?.message ?: "내 투표 내역이 없습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}