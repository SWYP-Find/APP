package com.picke.data.feature.recommend.repository

import com.picke.data.common.model.toResult
import com.picke.data.feature.recommend.datasource.RecommendApi
import com.picke.data.feature.recommend.model.toDomainModel
import com.picke.domain.feature.recommend.model.RecommendPageBoard
import com.picke.domain.feature.recommend.repository.RecommendRepository
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