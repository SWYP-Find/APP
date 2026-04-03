package com.picke.app.domain.repository

import com.picke.app.domain.model.CommentCreateBoard
import com.picke.app.domain.model.CommentLikeToggleBoard
import com.picke.app.domain.model.CommentPageBoard
import com.picke.app.domain.model.CommentUpdateBoard

interface CommentRepository {
    suspend fun getComments(perspectiveId: Long, cursor: String?, size: Int): Result<CommentPageBoard>
    suspend fun createComment(perspectiveId: Long, content: String): Result<CommentCreateBoard>
    suspend fun deleteComment(perspectiveId: Long, commentId: Long): Result<String>
    suspend fun updateComment(perspectiveId: Long, commentId: Long, content: String): Result<CommentUpdateBoard>
    suspend fun likeComment(commentId: Long): Result<CommentLikeToggleBoard>
    suspend fun unlikeComment(commentId: Long): Result<CommentLikeToggleBoard>
    suspend fun reportComment(perspectiveId: Long, commentId: Long): Result<String>
}