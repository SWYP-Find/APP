package com.picke.presentation.ui.comment.model

import com.picke.domain.feature.comment.model.CommentBoard
import com.picke.presentation.util.toRelativeTimeText

data class CommentUiModel(
    val commentId: String,
    val profileImageUrl: String,
    val nickname: String,
    val stance: String,
    val optionId: Long = 0L,
    val content: String,
    val timeAgo: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val replyCount: Int = 0
)

fun CommentBoard.toUiModel() = CommentUiModel(
    commentId = commentId.toString(),
    profileImageUrl = user.characterImageUrl,
    nickname = user.nickname,
    stance = stance,
    content = content,
    timeAgo = createdAt.toRelativeTimeText(),
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine
)