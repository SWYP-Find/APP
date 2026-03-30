package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.RecommendPageBoard

interface RecommendRepository {
    suspend fun getInterestingRecommendations(battleId: Long): Result<RecommendPageBoard>
}