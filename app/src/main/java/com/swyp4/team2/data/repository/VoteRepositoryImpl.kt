package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.VoteApi
import com.swyp4.team2.domain.model.VoteBoard
import com.swyp4.team2.domain.repository.VoteRepository
import javax.inject.Inject

class VoteRepositoryImpl @Inject constructor(
    private val voteApi: VoteApi
): VoteRepository{
    override suspend fun submitPreVote(battleId: String): Result<VoteBoard> {
        return try{
            val response = voteApi.submitPreVote(battleId)
            val responseData = response.data

            if(response.statusCode == 200 && responseData != null){
                Result.success(responseData.toDomainModel())
            } else{
                val errorMessage = response.error?.message ?: "투표를 제출하지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun submitPostVote(
        battleId: String,
        voteId: String
    ): Result<VoteBoard> {
        return try{
            val response = voteApi.submitPost(battleId)
            val responseData = response.data

            if(response.statusCode == 200 && responseData != null) {
                Result.success(responseData.toDomainModel())
            } else{
                val errorMessage = response.error?.message ?: "투표를 제출하지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}