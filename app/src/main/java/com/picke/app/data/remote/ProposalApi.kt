package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.ProposalRequestDto
import com.picke.app.data.model.ProposalResponseDto
import retrofit2.http.Body
import retrofit2.http.POST

interface ProposalApi {
    // 배틀 주제 제안
    @POST("/api/v1/battles/proposals")
    suspend fun submitProposal(
        @Body request: ProposalRequestDto
    ): BaseResponse<ProposalResponseDto>
}