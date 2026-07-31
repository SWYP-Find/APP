package com.picke.domain.usecase.share

import com.picke.domain.model.ShareKey
import com.picke.domain.repository.ShareRepository

class GetRecapShareKeyUseCase(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(): Result<ShareKey> {
        return shareRepository.getRecapShareKey()
    }
}
