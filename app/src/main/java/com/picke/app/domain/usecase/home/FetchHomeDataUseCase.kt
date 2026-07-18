package com.picke.app.domain.usecase.home

import com.picke.app.domain.model.HomeBoard
import com.picke.app.domain.repository.HomeRepository
import javax.inject.Inject

class FetchHomeDataUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {
    suspend operator fun invoke(): Result<HomeBoard> {
        return homeRepository.fetchHomeData()
    }
}
