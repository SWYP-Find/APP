package com.picke.app.domain.usecase

import com.picke.app.domain.model.MyVoteBoard
import com.picke.app.domain.repository.VoteRepository
import javax.inject.Inject

class GetMyVoteHistoryUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(battleId: Long): Result<MyVoteBoard> {
        return voteRepository.getMyVoteHistory(battleId)
    }
}
