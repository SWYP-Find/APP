package com.picke.domain.feature.vote.usecase

import com.picke.domain.feature.vote.repository.VoteRepository

sealed class SubmitVoteResult {
    data object Success : SubmitVoteResult()
    data object InsufficientPoints : SubmitVoteResult()
}

class SubmitVoteUseCase(
    private val voteRepository: VoteRepository
) {
    suspend operator fun invoke(
        battleId: Long,
        optionId: Long,
        isPreVote: Boolean
    ): Result<SubmitVoteResult> {
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
