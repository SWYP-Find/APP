package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.PerspectiveApi
import com.swyp4.team2.domain.model.PerspectivePage
import com.swyp4.team2.domain.repository.PerspectiveRepository
import javax.inject.Inject

class PerspectiveRepositoryImpl @Inject constructor(
    private val api: PerspectiveApi
) : PerspectiveRepository{
    override suspend fun getPerspectives(
        battleId: Long,
        cursor: String?,
        size: Int,
        optionLabel: String?
    ): Result<PerspectivePage> {
        return try {
            val response = api.getPerspectives(battleId, cursor, size, optionLabel)
            val responseData = response.data

            if ((response.statusCode == 200 || response.statusCode == 0) && responseData != null) {
                Result.success(responseData.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "관점 목록을 불러오지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}