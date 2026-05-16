package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.CommentRequestDto
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.model.toResult
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

    override suspend fun getComments(perspectiveId: Long, cursor: String?, size: Int): Result<CommentPageBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 목록 조회 시도 - perspectiveId: $perspectiveId, cursor: $cursor, size: $size")
            commentApi.getComments(perspectiveId, cursor, size)
                .toResult("댓글 목록을 불러오지 못했습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 댓글 목록 조회 성공 - 가져온 개수: ${domainData.items.size}, nextCursor: ${domainData.nextCursor}")
                    domainData.items.forEachIndexed { index, item ->
                        val shortContent = item.content.take(20).replace("\n", " ")
                        Log.d(TAG, "   └ [$index] commentId: ${item.commentId} | 입장(Stance): ${item.stance} | 작성자: ${item.user.nickname} | 내용: $shortContent...")
                    }
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 목록 조회 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun createComment(perspectiveId: Long, content: String): Result<CommentCreateBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 작성 시도 - perspectiveId: $perspectiveId, content: $content")
            commentApi.createComment(perspectiveId, CommentRequestDto(content))
                .toResult("댓글 작성에 실패했습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 댓글 작성 성공! - 생성된 댓글 ID: ${domainData.commentId}, 작성시간: ${domainData.createdAt}")
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 작성 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun deleteComment(perspectiveId: Long, commentId: Long): Result<String> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 삭제 시도 - perspectiveId: $perspectiveId, 삭제할 commentId: $commentId")
            commentApi.deleteComment(perspectiveId, commentId)
                .toResult("댓글 삭제에 실패했습니다.")
                .map { data ->
                    Log.d(TAG, "[API_RES] 댓글 삭제 성공 - 서버 응답: $data")
                    data
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 삭제 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun updateComment(perspectiveId: Long, commentId: Long, content: String): Result<CommentUpdateBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 수정 시도 - perspectiveId: $perspectiveId, commentId: $commentId, 수정할 내용: $content")
            commentApi.updateComment(perspectiveId, commentId, CommentRequestDto(content))
                .toResult("댓글 수정에 실패했습니다.")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 댓글 수정 성공! - 수정된 댓글 ID: ${domainData.commentId}, 수정시간: ${domainData.updatedAt}")
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 수정 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun likeComment(commentId: Long): Result<CommentLikeToggleBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 좋아요 등록 시도 - commentId: $commentId")
            commentApi.likeComment(commentId)
                .toResult("댓글 좋아요 등록 실패")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 댓글 좋아요 등록 성공 - 내 좋아요 여부: ${domainData.isLiked}, 현재 총 좋아요 수: ${domainData.likeCount}")
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 좋아요 등록 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun unlikeComment(commentId: Long): Result<CommentLikeToggleBoard> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 좋아요 취소 시도 - commentId: $commentId")
            commentApi.unlikeComment(commentId)
                .toResult("댓글 좋아요 취소 실패")
                .map { dto ->
                    val domainData = dto.toDomainModel()
                    Log.d(TAG, "[API_RES] 댓글 좋아요 취소 성공 - 내 좋아요 여부: ${domainData.isLiked}, 현재 총 좋아요 수: ${domainData.likeCount}")
                    domainData
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 댓글 좋아요 취소 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun reportComment(perspectiveId: Long, commentId: Long): Result<String> {
        return try {
            Log.d(TAG, "[API_REQ] 댓글 신고 시도 - perspectiveId: $perspectiveId, 신고할 commentId: $commentId")
            val response = commentApi.reportComment(perspectiveId, commentId)
            when (response.statusCode) {
                200 -> {
                    Log.d(TAG, "[API_RES] 댓글 신고 성공 - 서버 응답: ${response.data}")
                    Result.success(response.data ?: "Success")
                }
                409 -> {
                    Log.w(TAG, "[API_RES] 댓글 신고 중복 (이미 신고한 댓글입니다)")
                    Result.failure(Exception("ALREADY_REPORTED"))
                }
                else -> {
                    Log.e(TAG, "[API_RES] 댓글 신고 실패 - 에러: ${response.error?.message}")
                    Result.failure(Exception(response.error?.message ?: "댓글 신고에 실패했습니다."))
                }
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