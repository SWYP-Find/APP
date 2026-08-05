package com.picke.domain.feature.share.usecase

import com.picke.domain.feature.mypage.model.MyRecapBoard
import com.picke.domain.feature.share.repository.ShareRepository

class GetRecapDetailUseCase(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(shareKey: String): Result<MyRecapBoard> {
        return shareRepository.getRecapDetail(shareKey)
    }
}
