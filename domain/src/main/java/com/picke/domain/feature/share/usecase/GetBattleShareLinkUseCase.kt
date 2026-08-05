package com.picke.domain.feature.share.usecase

import com.picke.domain.feature.share.model.ShareUrl
import com.picke.domain.feature.share.repository.ShareRepository

class GetBattleShareLinkUseCase(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(battleId: Int): Result<ShareUrl> {
        return shareRepository.getBattleShareLink(battleId)
    }
}
