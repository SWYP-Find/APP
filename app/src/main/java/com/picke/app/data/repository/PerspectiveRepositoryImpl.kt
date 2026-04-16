package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.PerspectiveRequestDto
import com.picke.app.data.model.toDomainModel
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

    // 1. 관점 리스트 조회
    override suspend fun getPerspectives(
        battleId: Long, cursor: String?, size: Int, optionLabel: String?, sort: String
    ): Result<PerspectivePage> {
        return try {
            Log.d(TAG, "[API_REQ] 관점 목록 조회 시도 - battleId: $battleId, sort: $sort")
            val response = perspectiveApi.getPerspectives(battleId, cursor, size, optionLabel, sort)

            if ((response.statusCode == 200 || response.statusCode == 0) && response.data != null) {
                val domainData = response.data.toDomainModel()
                Log.d(TAG, "[API_RES] 관점 목록 조회 성공 - 총 ${domainData.items.size}개 수신")

                // 서버에서 내려준 Stance(입장)를 낱낱이 확인!
                domainData.items.forEachIndexed { index, item ->
                    val shortContent = item.content.take(15).replace("\n", " ")
                    Log.d(TAG, "   └ [$index] ID: ${item.commentId} | 입장(Stance): ${item.stance} | 닉네임: ${item.nickname} | 내용: $shortContent...")
                }
                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 관점 목록 조회 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "관점 목록을 불러오지 못했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 관점 목록 조회 예외: ${e.message}")
            Result.failure(e)
        }
    }

    // 2. 관점 작성
    override suspend fun createPerspective(battleId: Long, content: String): Result<PerspectiveStatusBoard> {
        return try {
            // ✨ 보시다시피 프론트는 content만 보냅니다! stance는 안 보냅니다.
            Log.d(TAG, "[API_REQ] 관점 작성 시도 - battleId: $battleId, 내용: $content")
            val response = perspectiveApi.createPerspective(battleId, PerspectiveRequestDto(content))

            if (response.data != null) {
                Log.d(TAG, "[API_RES] 관점 작성 성공 - 서버 응답 상태: ${response.data.status}")
                Result.success(response.data.toDomainModel())
            } else {
                Log.e(TAG, "[API_RES] 관점 작성 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "데이터가 없습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 관점 작성 예외: ${e.message}")
            Result.failure(e)
        }
    }

    // 3. 내 관점 조회
    override suspend fun getMyPerspective(battleId: Long): Result<PerspectiveDetailBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 내 관점 조회 시도 - battleId: $battleId")
            val response = perspectiveApi.getMyPerspective(battleId)

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                // ✨ 내 관점의 옵션 라벨(입장)이 무엇으로 내려오는지 확인!
                Log.d(TAG, "[API_RES] 내 관점 조회 성공 - ID: ${domainData.perspectiveId} | 내 입장(Label): ${domainData.optionLabel} | 상태: ${domainData.status}")
                Result.success(domainData)
            } else {
                Log.d(TAG, "[API_RES] 내 관점이 없습니다. (null 응답)")
                Result.failure(Exception(response.error?.message ?: "내 관점이 없습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 내 관점 조회 예외: ${e.message}")
            Result.failure(e)
        }
    }

    // 4. 관점 상세(단건) 조회 (댓글 진입 시 본문)
    override suspend fun getPerspective(perspectiveId: Long): Result<PerspectiveDetailBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 관점 상세(본문) 조회 시도 - perspectiveId: $perspectiveId")
            val response = perspectiveApi.getPerspective(perspectiveId)

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                // ✨ 댓글 화면의 본문 입장이 무엇으로 내려오는지 확인!
                Log.d(TAG, "[API_RES] 관점 상세 조회 성공 - 입장(Label): ${domainData.optionLabel} | 작성자: ${domainData.nickname}")
                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 관점 상세 조회 실패: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "상세 정보가 없습니다."))
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
            val data = response.data

            if (response.statusCode == 409) {
                Result.failure(Exception("ALREADY_REPORTED"))
            } else if (response.statusCode == 200 || response.statusCode == 0) {
                Result.success(data ?: "Success")
            } else {
                val errorMsg = response.error?.message ?: "알 수 없는 에러"
                Result.failure(Exception("신고 실패(Code: ${response.statusCode}): $errorMsg"))
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