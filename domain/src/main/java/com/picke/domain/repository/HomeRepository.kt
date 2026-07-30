package com.picke.domain.repository

import com.picke.domain.model.HomeBoard

interface HomeRepository {
    suspend fun fetchHomeData(): Result<HomeBoard>
}