package com.picke.presentation.ui.perspective.model

import com.picke.domain.feature.perspective.model.PerspectiveBoard
import com.picke.presentation.util.toRelativeTimeText

data class PerspectiveUiModel(
    val commentId: String,
    val profileImageUrl: String,
    val nickname: String,
    val optionTitle: String,
    val optionId: Long,
    val content: String,
    val timeAgo: String,
    val replyCount: Int,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean
)

fun PerspectiveBoard.toUiModel() = PerspectiveUiModel(
    commentId = commentId,
    profileImageUrl = characterImageUrl,
    nickname = nickname,
    optionTitle = optionTitle,
    optionId = optionId,
    content = content,
    timeAgo = createdAt.toRelativeTimeText(),
    replyCount = replyCount,
    likeCount = likeCount,
    isLiked = isLiked,
    isMine = isMine
)