package com.picke.app.domain.repository

import com.picke.app.domain.model.RecommendPageBoard

interface RecommendRepository {
    suspend fun getInterestingRecommendations(battleId: Long): Result<RecommendPageBoard>
}