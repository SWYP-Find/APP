package com.picke.app.domain.usecase.proposal

import com.picke.app.domain.repository.ProposalRepository
import retrofit2.HttpException
import javax.inject.Inject

sealed class SubmitProposalResult {
    data class Success(val proposalId: Long) : SubmitProposalResult()
    data object NotEnoughPoints : SubmitProposalResult()
}

class SubmitProposalUseCase @Inject constructor(
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
                if (error is HttpException && error.code() == 400) {
                    SubmitProposalResult.NotEnoughPoints
                } else {
                    throw error
                }
            }
    }
}
