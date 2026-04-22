package com.picke.app.data.repository

import com.picke.app.data.model.toResult
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.RecommendApi
import com.picke.app.domain.model.RecommendPageBoard
import com.picke.app.domain.repository.RecommendRepository
import javax.inject.Inject

class RecommendRepositoryImpl @Inject constructor(
    private val recommendApi: RecommendApi
) : RecommendRepository {
    override suspend fun getInterestingRecommendations(battleId: Long): Result<RecommendPageBoard> {
        return try {
            recommendApi.getInterestingRecommendations(battleId)
                .toResult("흥미로운 배틀 추천 목록을 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}