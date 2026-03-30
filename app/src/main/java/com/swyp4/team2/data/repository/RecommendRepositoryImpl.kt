package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.PerspectiveApi
import com.swyp4.team2.data.remote.RecommendApi
import com.swyp4.team2.domain.model.RecommendPageBoard
import com.swyp4.team2.domain.repository.PerspectiveRepository
import com.swyp4.team2.domain.repository.RecommendRepository
import javax.inject.Inject

class RecommendRepositoryImpl @Inject constructor(
    private val recommendApi: RecommendApi
) : RecommendRepository {
    override suspend fun getInterestingRecommendations(battleId: Long): Result<RecommendPageBoard> {
        return try {
            val response = recommendApi.getInterestingRecommendations(battleId)
            val data = response.data ?: throw Exception(response.error?.message ?: "추천 목록을 불러오지 못했습니다.")

            if((response.statusCode == 200) && data != null){
                Result.success(data.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "추천 목록을 불러오지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}