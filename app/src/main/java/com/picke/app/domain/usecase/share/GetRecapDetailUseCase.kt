package com.picke.app.domain.usecase.share

import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.repository.ShareRepository
import javax.inject.Inject

class GetRecapDetailUseCase @Inject constructor(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(shareKey: String): Result<MyRecapBoard> {
        return shareRepository.getRecapDetail(shareKey)
    }
}
