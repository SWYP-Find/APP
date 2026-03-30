package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.BaseResponse
import com.swyp4.team2.data.model.PerspectiveRequestDto
import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.PerspectiveApi
import com.swyp4.team2.domain.model.PerspectiveDetailBoard
import com.swyp4.team2.domain.model.PerspectiveLikeCountBoard
import com.swyp4.team2.domain.model.PerspectiveLikeToggleBoard
import com.swyp4.team2.domain.model.PerspectivePage
import com.swyp4.team2.domain.model.PerspectiveStatusBoard
import com.swyp4.team2.domain.model.PerspectiveUpdateBoard
import com.swyp4.team2.domain.repository.PerspectiveRepository
import javax.inject.Inject

class PerspectiveRepositoryImpl @Inject constructor(
    private val perspectiveApi: PerspectiveApi
) : PerspectiveRepository {

    // 1. 관점 리스트 조회
    override suspend fun getPerspectives(
        battleId: Long,
        cursor: String?,
        size: Int,
        optionLabel: String?,
        sort: String
    ): Result<PerspectivePage> {
        return try {
            val response = perspectiveApi.getPerspectives(battleId, cursor, size, optionLabel, sort)
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

    override suspend fun createPerspective(battleId: Long, content: String): Result<PerspectiveStatusBoard> {
        return try {
            val response = perspectiveApi.createPerspective(battleId, PerspectiveRequestDto(content))
            val data = response.data ?: throw Exception(response.error?.message ?: "데이터가 없습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getMyPerspective(battleId: Long): Result<PerspectiveDetailBoard> {
        return try {
            val response = perspectiveApi.getMyPerspective(battleId)
            val data = response.data ?: throw Exception(response.error?.message ?: "내 관점이 없습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPerspective(perspectiveId: Long): Result<PerspectiveDetailBoard> {
        return try {
            val response = perspectiveApi.getPerspective(perspectiveId)
            val data = response.data ?: throw Exception(response.error?.message ?: "관점 상세 정보가 없습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deletePerspective(perspectiveId: Long): Result<String> {
        return try {
            val response = perspectiveApi.deletePerspective(perspectiveId)
            val data = response.data ?: throw Exception(response.error?.message ?: "삭제 실패")
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updatePerspective(perspectiveId: Long, content: String): Result<PerspectiveUpdateBoard> {
        return try {
            val response = perspectiveApi.updatePerspective(perspectiveId, PerspectiveRequestDto(content))
            val data = response.data ?: throw Exception(response.error?.message ?: "수정 실패")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun retryModeration(perspectiveId: Long): Result<String> {
        return try {
            val response = perspectiveApi.retryModeration(perspectiveId)
            val data = response.data ?: throw Exception(response.error?.message ?: "재시도 요청 실패")
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getPerspectiveLikeCount(perspectiveId: Long): Result<PerspectiveLikeCountBoard> {
        return try {
            val response = perspectiveApi.getPerspectiveLikeCount(perspectiveId)
            val data = response.data ?: throw Exception(response.error?.message ?: "좋아요 수 조회 실패")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun likePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard> {
        return try {
            val response = perspectiveApi.likePerspective(perspectiveId)
            val data = response.data ?: throw Exception(response.error?.message ?: "좋아요 등록 실패")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unlikePerspective(perspectiveId: Long): Result<PerspectiveLikeToggleBoard> {
        return try {
            val response = perspectiveApi.unlikePerspective(perspectiveId)
            val data = response.data ?: throw Exception(response.error?.message ?: "좋아요 취소 실패")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reportPerspective(perspectiveId: Long): Result<String> {
        return try {
            val response = perspectiveApi.reportPerspective(perspectiveId)
            val data = response.data ?: throw Exception(response.error?.message ?: "관점 신고에 실패했습니다.")
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}