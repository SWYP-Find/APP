package com.picke.domain.feature.proposal.repository

import com.picke.domain.feature.proposal.model.ProposalBoard

interface ProposalRepository {
    suspend fun submitProposal(
        category: String,
        topic: String,
        positionA: String,
        positionB: String,
        description: String
    ): Result<ProposalBoard>
}