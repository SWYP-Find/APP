package com.picke.presentation.util

import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.explore.model.ExploreUiModel

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

    val dummyExploreList = listOf(
        ExploreUiModel(
            battleId = "",
            thumbnailUrl = "",
            type = "",
            title = "test explore1",
            summary = "test summary1",
            tags = listOf("tag1", "tag2", "tag3"),
            audioDurationText = "test audio duration text1",
            viewCountText = "view count text1"
        ),
        ExploreUiModel(
            battleId = "",
            thumbnailUrl = "",
            type = "",
            title = "test explore2",
            summary = "test summary2",
            tags = listOf("tag1"),
            audioDurationText = "test audio duration text2",
            viewCountText = "view count text2"
        ),
        ExploreUiModel(
            battleId = "",
            thumbnailUrl = "",
            type = "",
            title = "test explore3",
            summary = "test summary3",
            tags = listOf("tag2", "tag3"),
            audioDurationText = "test audio duration text3",
            viewCountText = "3"
        )
    )
}