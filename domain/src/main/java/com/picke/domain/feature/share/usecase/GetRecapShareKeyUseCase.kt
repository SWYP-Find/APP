package com.picke.domain.feature.share.usecase

import com.picke.domain.feature.share.model.ShareKey
import com.picke.domain.feature.share.repository.ShareRepository

class GetRecapShareKeyUseCase(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(): Result<ShareKey> {
        return shareRepository.getRecapShareKey()
    }
}
