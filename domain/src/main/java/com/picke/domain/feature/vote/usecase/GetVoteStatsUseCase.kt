package com.picke.domain.feature.vote.usecase

import com.picke.domain.feature.vote.model.VoteStatsBoard
import com.picke.domain.feature.vote.repository.VoteRepository

class GetVoteStatsUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(battleId: Long): Result<VoteStatsBoard> {
        return voteRepository.getVoteStats(battleId)
    }
}
