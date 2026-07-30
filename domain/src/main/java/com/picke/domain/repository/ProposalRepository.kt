package com.picke.domain.repository

import com.picke.domain.model.ProposalBoard

interface ProposalRepository {
    suspend fun submitProposal(
        category: String,
        topic: String,
        positionA: String,
        positionB: String,
        description: String
    ): Result<ProposalBoard>
}