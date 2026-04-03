package com.picke.app.domain.repository

import com.picke.app.domain.model.HomeBoard

interface HomeRepository {
    suspend fun fetchHomeData(): Result<HomeBoard>
}