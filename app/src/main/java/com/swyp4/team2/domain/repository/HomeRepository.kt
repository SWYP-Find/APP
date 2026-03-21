package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.HomeBoardData

interface HomeRepository {
    suspend fun fetchHomeData(): Result<HomeBoardData>
}