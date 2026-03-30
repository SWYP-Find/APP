package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.CommentRequestDto
import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.CommentApi
import com.swyp4.team2.domain.model.CommentCreateBoard
import com.swyp4.team2.domain.model.CommentLikeToggleBoard
import com.swyp4.team2.domain.model.CommentPageBoard
import com.swyp4.team2.domain.model.CommentUpdateBoard
import com.swyp4.team2.domain.repository.CommentRepository
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
            val data = response.data ?: throw Exception(response.error?.message ?: "댓글 신고에 실패했습니다.")
            Result.success(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}