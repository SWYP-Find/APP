package com.picke.domain.usecase.share

import com.picke.domain.model.ShareUrl
import com.picke.domain.repository.ShareRepository

class GetBattleShareLinkUseCase(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(battleId: Int): Result<ShareUrl> {
        return shareRepository.getBattleShareLink(battleId)
    }
}
