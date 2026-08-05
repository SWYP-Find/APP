package com.picke.domain.feature.home.usecase

import com.picke.domain.feature.home.model.HomeBoard
import com.picke.domain.feature.home.repository.HomeRepository

class FetchHomeDataUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeBoard> {
        return homeRepository.fetchHomeData()
    }
}
