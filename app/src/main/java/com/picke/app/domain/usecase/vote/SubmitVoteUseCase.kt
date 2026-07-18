package com.picke.app.domain.usecase.vote

import com.picke.app.domain.repository.VoteRepository
import javax.inject.Inject

sealed class SubmitVoteResult {
    data object Success : SubmitVoteResult()
    data object InsufficientPoints : SubmitVoteResult()
}

class SubmitVoteUseCase @Inject constructor(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(battleId: Long, optionId: Long, isPreVote: Boolean): Result<SubmitVoteResult> {
        val result = if (isPreVote) {
            voteRepository.submitPreVote(battleId, optionId)
        } else {
            voteRepository.submitPostVote(battleId, optionId)
        }

        return result.map { SubmitVoteResult.Success as SubmitVoteResult }
            .recoverCatching { error ->
                if (error.message?.contains("CREDIT_400_INSUFFICIENT") == true) {
                    SubmitVoteResult.InsufficientPoints
                } else {
                    throw error
                }
            }
    }
}
