package com.picke.app.domain.repository

import com.picke.app.domain.model.ProposalBoard

interface ProposalRepository {
    suspend fun submitProposal(
        category: String,
        topic: String,
        positionA: String,
        positionB: String,
        description: String
    ): Result<ProposalBoard>
}