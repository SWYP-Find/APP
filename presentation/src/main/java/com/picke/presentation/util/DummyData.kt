package com.picke.presentation.util

import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.explore.model.ExploreUiModel
import com.picke.presentation.ui.home.model.ContentUiType
import com.picke.presentation.ui.home.model.HomeContentUiModel
import com.picke.presentation.ui.home.model.PollQuizOptionStatUiModel
import com.picke.presentation.ui.home.model.TodayPickUiModel

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

    val dummyHomeContentItems = listOf(
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.BATTLE,
            title = "best battle title1",
            summary = "best battle summary1",
            thumbnailUrl = "",
            viewCountText = "123",
            timeInfoText = "best battle time info text1",
            tags = emptyList()
        ),
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.QUIZ,
            title = "best battle title2",
            summary = "best battle summary2",
            thumbnailUrl = "",
            viewCountText = "123456",
            timeInfoText = "best battle time info text2",
            tags = emptyList()
        ),
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.VOTE,
            title = "best battle title3",
            summary = "best battle summary3",
            thumbnailUrl = "",
            viewCountText = "1",
            timeInfoText = "best battle time info text3",
            tags = emptyList()
        ),
        HomeContentUiModel(
            contentId = "",
            type = ContentUiType.UNKNOWN,
            title = "best battle title4",
            summary = "best battle summary4",
            thumbnailUrl = "",
            viewCountText = "111",
            timeInfoText = "best battle time info text4",
            tags = emptyList()
        )
    )

    val dummyVotePick = TodayPickUiModel.VotePick(
        contentId = "vote_001",
        titlePrefix = "Q. ",
        title = "평생 한 가지 음식만 먹어야 한다면?",
        titleSuffix = "",
        summary = "짜장면 vs 짬뽕, 당신의 영혼의 단짝은?",
        participantsCount = 1542,
        selectedOptionId = null,
        type = "VOTE",
        options = listOf(
            PollQuizOptionStatUiModel(
                optionId = 1L,
                title = "윤기 좔좔 짜장면",
                isCorrect = false,
                stance = "짜장면",
                voteCount = 848,
                ratio = 55.0f
            ),
            PollQuizOptionStatUiModel(
                optionId = 2L,
                title = "얼큰한 국물 짬뽕",
                isCorrect = false,
                stance = "짬뽕",
                voteCount = 694,
                ratio = 45.0f
            )
        )
    )

    val dummyQuizPick = TodayPickUiModel.QuizPick(
        contentId = "quiz_001",
        title = "다음 중 안드로이드의 공식 마스코트 이름은?",
        summary = "알쏭달쏭 IT 상식 퀴즈",
        participantsCount = 820,
        selectedOptionId = 1L,
        type = "QUIZ",
        options = listOf(
            PollQuizOptionStatUiModel(
                optionId = 1L,
                title = "버그드로이드 (Bugdroid)",
                isCorrect = true,
                stance = "NONE",
                voteCount = 697,
                ratio = 85.0f
            ),
            PollQuizOptionStatUiModel(
                optionId = 2L,
                title = "안디 (Andy)",
                isCorrect = false,
                stance = "NONE",
                voteCount = 82,
                ratio = 10.0f
            ),
            PollQuizOptionStatUiModel(
                optionId = 3L,
                title = "로보 (Robo)",
                isCorrect = false,
                stance = "NONE",
                voteCount = 41,
                ratio = 5.0f
            )
        )
    )
}