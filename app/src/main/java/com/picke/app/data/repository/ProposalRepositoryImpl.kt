package com.picke.app.data.repository

import com.picke.app.data.model.ProposalRequestDto
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.ProposalApi
import com.picke.app.domain.model.ProposalBoard
import com.picke.app.domain.repository.ProposalRepository
import javax.inject.Inject

class ProposalRepositoryImpl @Inject constructor(
    private val proposalApi: ProposalApi
) : ProposalRepository {

    override suspend fun submitProposal(
        category: String,
        topic: String,
        positionA: String,
        positionB: String,
        description: String
    ): Result<ProposalBoard> {
        return try {
            val request = ProposalRequestDto(
                category = category,
                topic = topic,
                positionA = positionA,
                positionB = positionB,
                description = description
            )

            val response = proposalApi.submitProposal(request)
            val data = response.data ?: throw Exception(response.error?.message ?: "주제 제안에 실패했습니다.")

            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}