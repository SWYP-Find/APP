package com.picke.domain.feature.recommend.repository

import com.picke.domain.feature.recommend.model.RecommendPageBoard

interface RecommendRepository {
    suspend fun getInterestingRecommendations(battleId: Long): Result<RecommendPageBoard>
}