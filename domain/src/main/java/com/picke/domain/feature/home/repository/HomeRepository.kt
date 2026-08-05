package com.picke.domain.feature.home.repository

import com.picke.domain.feature.home.model.HomeBoard

interface HomeRepository {
    suspend fun fetchHomeData(): Result<HomeBoard>
}