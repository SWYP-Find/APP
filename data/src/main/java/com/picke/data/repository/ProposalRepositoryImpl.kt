package com.picke.data.repository

import com.picke.data.model.ProposalRequestDto
import com.picke.data.model.toDomainModel
import com.picke.data.remote.ProposalApi
import com.picke.domain.exception.NotEnoughPointsException
import com.picke.domain.model.ProposalBoard
import com.picke.domain.repository.ProposalRepository
import retrofit2.HttpException
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
        } catch (e: HttpException) {
            if (e.code() == 400) {
                Result.failure(NotEnoughPointsException())
            } else {
                Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}