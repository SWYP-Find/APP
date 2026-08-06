package com.picke.presentation.util

import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.presentation.ui.comment.model.CommentUiModel

object DummyData {

    val dummyAlarmList = listOf(
        AlarmItemBoard(
            notificationId = 0,
            perspectiveId = 0,
            category = "ALL",
            detailCode = "",
            title = "test title1",
            body = "test body1",
            referenceId = 0,
            isRead = true,
            createdAt = ""
        ),
        AlarmItemBoard(
            notificationId = 1,
            perspectiveId = 1,
            category = "CONTENT",
            detailCode = "",
            title = "test title2 test title2 test title2 test title2",
            body = "test body2 test body2 test body2 test body2 test body2 test body2 test body2 test body2 test body2",
            referenceId = 1,
            isRead = true,
            createdAt = ""
        ),
        AlarmItemBoard(
            notificationId = 2,
            perspectiveId = 2,
            category = "NOTICE",
            detailCode = "",
            title = "test title3",
            body = "test body3",
            referenceId = 2,
            isRead = false,
            createdAt = ""
        )
    )

    val dummyComments = listOf(
        CommentUiModel(
            commentId = "",
            profileImageUrl = "",
            nickname = "",
            stance = "",
            content = "test comment1",
            timeAgo = "",
            likeCount = 10,
            isLiked = true,
            isMine = false
        ),
        CommentUiModel(
            commentId = "",
            profileImageUrl = "",
            nickname = "",
            stance = "",
            content = "test comment2",
            timeAgo = "",
            likeCount = 123,
            isLiked = true,
            isMine = false
        ),
        CommentUiModel(
            commentId = "",
            profileImageUrl = "",
            nickname = "",
            stance = "",
            content = "test comment3 test comment3 test comment3",
            timeAgo = "",
            likeCount = 3000,
            isLiked = false,
            isMine = false
        ),
        CommentUiModel(
            commentId = "",
            profileImageUrl = "",
            nickname = "",
            stance = "",
            content = "test comment4",
            timeAgo = "",
            likeCount = 100,
            isLiked = false,
            isMine = true
        ),
        CommentUiModel(
            commentId = "",
            profileImageUrl = "",
            nickname = "",
            stance = "",
            content = "test comment5 test comment5 test comment5 test comment5 test comment5",
            timeAgo = "",
            likeCount = 0,
            isLiked = true,
            isMine = true
        )
    )
}