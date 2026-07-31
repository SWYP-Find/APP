package com.picke.domain.usecase.vote

import com.picke.domain.model.MyVoteBoard
import com.picke.domain.repository.VoteRepository

class GetMyVoteHistoryUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(battleId: Long): Result<MyVoteBoard> {
        return voteRepository.getMyVoteHistory(battleId)
    }
}
