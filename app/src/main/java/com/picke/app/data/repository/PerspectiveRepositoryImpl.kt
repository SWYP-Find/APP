package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.PerspectiveRequestDto
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.model.toResult
import com.picke.app.data.remote.PerspectiveApi
import com.picke.app.domain.model.PerspectiveDetailBoard
import com.picke.app.domain.model.PerspectiveLikeCountBoard
import com.picke.app.domain.model.PerspectiveLikeToggleBoard
import com.picke.app.domain.model.PerspectivePage
import com.picke.app.domain.model.PerspectiveStatusBoard
import com.picke.app.domain.model.PerspectiveUpdateBoard
import com.picke.app.domain.repository.PerspectiveRepository
import javax.inject.Inject

class PerspectiveRepositoryImpl @Inject constructor(
    private val perspectiveApi: PerspectiveApi
) : PerspectiveRepository {

    companion object {
        private const val TAG = "PerspectiveRepo_Picke"
    }

    override suspend fun getPerspectives(
        battleId: Long, cursor: String?, size: Int, optionLabel: String?, sort: String
    ): Result<PerspectivePage> {
        return try {
            Log.d(TAG, "[API_REQ] 관점 목록 조회 시도 - battleId: $battleId, sort: $sort")
            perspectiveApi.getPerspectives(battleId, cursor, size, optionLabel, sort)
                .toResult("관점 목록을 불러오지 못했습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 관점 목록 조회 성공 - 총 ${domainData.items.size}개 수신")
                    domainData.items.forEachIndexed { index, item ->
                        val shortContent = item.content.take(15).replace("\n", " ")
                        Log.d(TAG, "   └ [$index] ID: ${item.commentId} | 입장(Stance): ${item.stance} | 닉네임: ${item.nickname} | 내용: $shortContent...")
                    }
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 관점 목록 조회 예외: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun createPerspective(battleId: Long, content: String): Result<PerspectiveStatusBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 관점 작성 시도 - battleId: $battleId, 내용: $content")
            perspectiveApi.createPerspective(battleId, PerspectiveRequestDto(content))
                .toResult("관점 작성에 실패했습니다.")
                .map { dto ->
                    Log.d(TAG, "[API_RES] 관점 작성 성공 - 서버 응답 상태: ${dto.status}")
                    dto.toDomainModel()
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 관점 작성 예외: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getMyPerspective(battleId: Long): Result<PerspectiveDetailBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 내 관점 조회 시도 - battleId: $battleId")
            perspectiveApi.getMyPerspective(battleId)
                .toResult("내 관점이 없습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 내 관점 조회 성공 - ID: ${domainData.perspectiveId} | 내 입장(Label): ${domainData.optionLabel} | 상태: ${domainData.status}")
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 내 관점 조회 예외: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getPerspective(perspectiveId: Long): Result<PerspectiveDetailBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 관점 상세(본문) 조회 시도 - perspectiveId: $perspectiveId")
            perspectiveApi.getPerspective(perspectiveId)
                .toResult("상세 정보가 없습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 관점 상세 조회 성공 - 입장(Label): ${domainData.optionLabel} | 작성자: ${domainData.nickname}")
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 관점 상세 조회 예외: ${e.message}")
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
            when (response.statusCode) {
                200 -> Result.success(response.data ?: "Success")
                409 -> Result.failure(Exception("ALREADY_REPORTED"))
                else -> Result.failure(Exception("신고 실패(Code: ${response.statusCode}): ${response.error?.message ?: "알 수 없는 에러"}"))
            }
        } catch (e: Exception) {
            if (e.message?.contains("409") == true) {
                Result.failure(Exception("ALREADY_REPORTED"))
            } else {
                Result.failure(e)
            }
        }
    }
}