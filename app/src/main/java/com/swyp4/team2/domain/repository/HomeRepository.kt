package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.HomeBoard

interface HomeRepository {
    suspend fun fetchHomeData(): Result<HomeBoard>
}