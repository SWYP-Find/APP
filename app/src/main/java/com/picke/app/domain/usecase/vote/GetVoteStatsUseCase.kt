package com.picke.app.domain.usecase.vote

import com.picke.app.domain.model.VoteStatsBoard
import com.picke.app.domain.repository.VoteRepository
import javax.inject.Inject

class GetVoteStatsUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(battleId: Long): Result<VoteStatsBoard> {
        return voteRepository.getVoteStats(battleId)
    }
}
