package com.picke.domain.feature.proposal.usecase

import com.picke.domain.common.exception.NotEnoughPointsException
import com.picke.domain.feature.proposal.repository.ProposalRepository

sealed class SubmitProposalResult {
    data class Success(val proposalId: Long) : SubmitProposalResult()
    data object NotEnoughPoints : SubmitProposalResult()
}

class SubmitProposalUseCase(
    private val proposalRepository: ProposalRepository
) {
    suspend operator fun invoke(
        category: String,
        topic: String,
        stanceA: String,
        stanceB: String,
        description: String
    ): Result<SubmitProposalResult> {
        return proposalRepository.submitProposal(
            category = category,
            topic = topic,
            positionA = stanceA,
            positionB = stanceB,
            description = description
        )
            .map { SubmitProposalResult.Success(it.id) as SubmitProposalResult }
            .recoverCatching { error ->
                if (error is NotEnoughPointsException) {
                    SubmitProposalResult.NotEnoughPoints
                } else {
                    throw error
                }
            }
    }
}
