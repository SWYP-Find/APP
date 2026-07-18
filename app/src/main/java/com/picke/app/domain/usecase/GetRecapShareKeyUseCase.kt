package com.picke.app.domain.usecase

import com.picke.app.domain.model.ShareKey
import com.picke.app.domain.repository.ShareRepository
import javax.inject.Inject

class GetRecapShareKeyUseCase @Inject constructor(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(): Result<ShareKey> {
        return shareRepository.getRecapShareKey()
    }
}
