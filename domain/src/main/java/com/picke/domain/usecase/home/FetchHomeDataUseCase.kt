package com.picke.domain.usecase.home

import com.picke.domain.model.HomeBoard
import com.picke.domain.repository.HomeRepository

class FetchHomeDataUseCase(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeBoard> {
        return homeRepository.fetchHomeData()
    }
}
