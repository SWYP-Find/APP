package com.picke.domain.usecase.share

import com.picke.domain.model.MyRecapBoard
import com.picke.domain.repository.ShareRepository

class GetRecapDetailUseCase(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(shareKey: String): Result<MyRecapBoard> {
        return shareRepository.getRecapDetail(shareKey)
    }
}
