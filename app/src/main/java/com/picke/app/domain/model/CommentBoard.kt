package com.picke.app.domain.model

data class CommentUserBoard(
    val userTag: String,
    val nickname: String,
    val characterType: String,
    val characterImageUrl: String
)

data class CommentBoard(
    val commentId: Long,
    val user: CommentUserBoard,
    val stance: String,
    val content: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val createdAt: String
)

data class CommentPageBoard(
    val items: List<CommentBoard>,
    val nextCursor: String?,
    val hasNext: Boolean
)

data class CommentCreateBoard(
    val commentId: Long,
    val user: CommentUserBoard,
    val stance: String,
    val content: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val createdAt: String
)

data class CommentUpdateBoard(
    val commentId: Long,
    val content: String,
    val updatedAt: String
)

// 댓글 좋아요 등록/취소 결과 모델
data class CommentLikeToggleBoard(
    val perspectiveId: Long,
    val likeCount: Int,
    val isLiked: Boolean
)