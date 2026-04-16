package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.CommentRequestDto
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.CommentApi
import com.picke.app.domain.model.CommentCreateBoard
import com.picke.app.domain.model.CommentLikeToggleBoard
import com.picke.app.domain.model.CommentPageBoard
import com.picke.app.domain.model.CommentUpdateBoard
import com.picke.app.domain.repository.CommentRepository
import javax.inject.Inject

class CommentRepositoryImpl @Inject constructor(
    private val commentApi: CommentApi
) : CommentRepository {

    companion object {
        private const val TAG = "CommentRepositoryImpl_Picke"
    }

    // 1. 댓글 목록 페이징 조회 (상세 내용물 로그 추가 ✨)
    override suspend fun getComments(perspectiveId: Long, cursor: String?, size: Int): Result<CommentPageBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 목록 조회 시도 - perspectiveId: $perspectiveId, cursor: $cursor, size: $size")
            val response = commentApi.getComments(perspectiveId, cursor, size)

            if (response.data != null) {
                val domainData = response.data.toDomainModel()

                Log.d(TAG, "[API_RES] 댓글 목록 조회 성공 - 가져온 개수: ${domainData.items.size}, nextCursor: ${domainData.nextCursor}")

                // 여기서 서버가 준 진짜 내용물(입장, 작성자, 내용)을 하나씩 다 까서 로그로 찍어줍니다!
                domainData.items.forEachIndexed { index, item ->
                    // 가독성을 위해 본문이 너무 길면 20자까지만 자르고 줄바꿈을 공백으로 바꿉니다.
                    val shortContent = item.content.take(20).replace("\n", " ")
                    Log.d(TAG, "   └ [$index] ID: ${item.commentId} | 입장(Stance): ${item.stance} | 작성자: ${item.user.nickname} | 내용: $shortContent...")
                }

                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 댓글 목록 조회 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "데이터가 없습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 목록 조회 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 2. 댓글 작성
    override suspend fun createComment(perspectiveId: Long, content: String): Result<CommentCreateBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 작성 시도 - perspectiveId: $perspectiveId, content(내용): $content")
            val response = commentApi.createComment(perspectiveId, CommentRequestDto(content))

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                Log.d(TAG, "[API_RES] 댓글 작성 성공! - 생성된 댓글 ID: ${domainData.commentId}, 작성시간: ${domainData.createdAt}")
                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 댓글 작성 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "댓글 작성에 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 작성 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 3. 댓글 삭제
    override suspend fun deleteComment(perspectiveId: Long, commentId: Long): Result<String> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 삭제 시도 - perspectiveId: $perspectiveId, 삭제할 commentId: $commentId")
            val response = commentApi.deleteComment(perspectiveId, commentId)

            if (response.data != null) {
                Log.d(TAG, "[API_RES] 댓글 삭제 성공 - 서버 응답: ${response.data}")
                Result.success(response.data)
            } else {
                Log.e(TAG, "[API_RES] 댓글 삭제 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "댓글 삭제에 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 삭제 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 4. 댓글 수정
    override suspend fun updateComment(perspectiveId: Long, commentId: Long, content: String): Result<CommentUpdateBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 수정 시도 - perspectiveId: $perspectiveId, commentId: $commentId, 수정할 내용: $content")
            val response = commentApi.updateComment(perspectiveId, commentId, CommentRequestDto(content))

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                Log.d(TAG, "[API_RES] 댓글 수정 성공! - 수정된 댓글 ID: ${domainData.commentId}, 수정시간: ${domainData.updatedAt}")
                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 댓글 수정 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "댓글 수정에 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 수정 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 5. 댓글 좋아요 등록
    override suspend fun likeComment(commentId: Long): Result<CommentLikeToggleBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 좋아요 등록 시도 - 좋아요 누를 commentId: $commentId")
            val response = commentApi.likeComment(commentId)

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                Log.d(TAG, "[API_RES] 댓글 좋아요 등록 성공 - 내 좋아요 여부: ${domainData.isLiked}, 현재 총 좋아요 수: ${domainData.likeCount}")
                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 댓글 좋아요 등록 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "댓글 좋아요 등록 실패"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 좋아요 등록 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 6. 댓글 좋아요 취소
    override suspend fun unlikeComment(commentId: Long): Result<CommentLikeToggleBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 좋아요 취소 시도 - 좋아요 취소할 commentId: $commentId")
            val response = commentApi.unlikeComment(commentId)

            if (response.data != null) {
                val domainData = response.data.toDomainModel()
                Log.d(TAG, "[API_RES] 댓글 좋아요 취소 성공 - 내 좋아요 여부: ${domainData.isLiked}, 현재 총 좋아요 수: ${domainData.likeCount}")
                Result.success(domainData)
            } else {
                Log.e(TAG, "[API_RES] 댓글 좋아요 취소 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "댓글 좋아요 취소 실패"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 좋아요 취소 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    // 7. 댓글 신고
    override suspend fun reportComment(perspectiveId: Long, commentId: Long): Result<String> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 신고 시도 - perspectiveId: $perspectiveId, 신고할 commentId: $commentId")
            val response = commentApi.reportComment(perspectiveId, commentId)

            if (response.statusCode == 409) {
                Log.w(TAG, "[API_RES] 댓글 신고 중복 (이미 신고한 댓글입니다)")
                Result.failure(Exception("ALREADY_REPORTED"))
            } else if (response.statusCode == 200 || response.statusCode == 0) {
                Log.d(TAG, "[API_RES] 댓글 신고 성공 - 서버 응답: ${response.data}")
                Result.success(response.data ?: "Success")
            } else {
                Log.e(TAG, "[API_RES] 댓글 신고 실패 - 에러: ${response.error?.message}")
                Result.failure(Exception(response.error?.message ?: "댓글 신고에 실패했습니다."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 신고 예외 발생: ${e.message}")
            if (e.message?.contains("409") == true) {
                Result.failure(Exception("ALREADY_REPORTED"))
            } else {
                Result.failure(e)
            }
        }
    }
}