package com.picke.app.domain.usecase.share

import com.picke.app.domain.model.ShareUrl
import com.picke.app.domain.repository.ShareRepository
import javax.inject.Inject

class GetBattleShareLinkUseCase @Inject constructor(
    private val shareRepository: ShareRepository
) {
    suspend operator fun invoke(battleId: Int): Result<ShareUrl> {
        return shareRepository.getBattleShareLink(battleId)
    }
}
