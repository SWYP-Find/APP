package com.picke.domain.repository

import com.picke.domain.model.RecommendPageBoard

interface RecommendRepository {
    suspend fun getInterestingRecommendations(battleId: Long): Result<RecommendPageBoard>
}