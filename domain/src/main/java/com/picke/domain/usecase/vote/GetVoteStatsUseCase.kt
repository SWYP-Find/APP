package com.picke.domain.usecase.vote

import com.picke.domain.model.VoteStatsBoard
import com.picke.domain.repository.VoteRepository

class GetVoteStatsUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(battleId: Long): Result<VoteStatsBoard> {
        return voteRepository.getVoteStats(battleId)
    }
}
