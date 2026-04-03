package com.picke.app.data.repository

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

    override suspend fun getComments(perspectiveId: Long, cursor: String?, size: Int): Result<CommentPageBoard> {
        return try {
            val response = commentApi.getComments(perspectiveId, cursor, size)
            val data = response.data ?: throw Exception(response.error?.message ?: "데이터가 없습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createComment(perspectiveId: Long, content: String): Result<CommentCreateBoard> {
        return try {
            val response = commentApi.createComment(perspectiveId, CommentRequestDto(content))
            val data = response.data ?: throw Exception(response.error?.message ?: "댓글 작성에 실패했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteComment(perspectiveId: Long, commentId: Long): Result<String> {
        return try {
            val response = commentApi.deleteComment(perspectiveId, commentId)
            val data = response.data ?: throw Exception(response.error?.message ?: "댓글 삭제에 실패했습니다.")
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateComment(perspectiveId: Long, commentId: Long, content: String): Result<CommentUpdateBoard> {
        return try {
            val response = commentApi.updateComment(perspectiveId, commentId, CommentRequestDto(content))
            val data = response.data ?: throw Exception(response.error?.message ?: "댓글 수정에 실패했습니다.")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun likeComment(commentId: Long): Result<CommentLikeToggleBoard> {
        return try {
            val response = commentApi.likeComment(commentId)
            val data = response.data ?: throw Exception(response.error?.message ?: "댓글 좋아요 등록 실패")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unlikeComment(commentId: Long): Result<CommentLikeToggleBoard> {
        return try {
            val response = commentApi.unlikeComment(commentId)
            val data = response.data ?: throw Exception(response.error?.message ?: "댓글 좋아요 취소 실패")
            Result.success(data.toDomainModel())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun reportComment(
        perspectiveId: Long,
        commentId: Long
    ): Result<String> {
        return try {
            val response = commentApi.reportComment(perspectiveId, commentId)

            if (response.statusCode == 409) {
                Result.failure(Exception("ALREADY_REPORTED"))
            } else if (response.statusCode == 200 || response.statusCode == 0) {
                Result.success(response.data ?: "Success")
            } else {
                Result.failure(Exception(response.error?.message ?: "댓글 신고에 실패했습니다."))
            }
        } catch (e: Exception) {
            // 만약 Retrofit HttpException이 직접 발생하는 구조라면 여기서도 체크
            if (e.message?.contains("409") == true) {
                Result.failure(Exception("ALREADY_REPORTED"))
            } else {
                Result.failure(e)
            }
        }
    }
}