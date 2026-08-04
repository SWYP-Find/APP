package com.picke.data.feature.proposal.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.proposal.model.ProposalRequestDto
import com.picke.data.feature.proposal.model.ProposalResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ProposalApi {
    // 배틀 주제 제안
    @POST("/api/v1/battles/proposals")
    suspend fun submitProposal(
        @Body request: ProposalRequestDto
    ): BaseResponse<ProposalResponseDto>
}