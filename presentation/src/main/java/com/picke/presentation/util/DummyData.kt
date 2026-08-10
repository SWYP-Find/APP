package com.picke.presentation.util

import com.picke.domain.feature.alarm.model.AlarmItemBoard
import com.picke.domain.feature.scenario.model.SpeakerType
import com.picke.domain.feature.vote.model.VoteStatsOptionBoard
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.explore.model.ExploreUiModel
import com.picke.presentation.ui.home.model.ContentUiType
import com.picke.presentation.ui.home.model.HomeContentUiModel
import com.picke.presentation.ui.home.model.PollQuizOptionStatUiModel
import com.picke.presentation.ui.home.model.TodayPickUiModel
import com.picke.presentation.ui.perspective.model.PerspectiveUiModel
import com.picke.presentation.ui.recommend.model.RecommendUiModel
import com.picke.presentation.ui.scenario.model.PastChoice
import com.picke.presentation.ui.scenario.model.ScenarioOptionUiModel
import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel

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

    val dummyVoteOptions = listOf(
        VoteStatsOptionBoard(
            optionId = 1L,
            title = "민초 극호",
            imageUrl = "",
            isCorrect = false,
            voteCount = 1520,
            ratio = 65.0f,
            stance = "PRO"
        ),
        VoteStatsOptionBoard(
            optionId = 2L,
            title = "민초 극불호",
            imageUrl = "",
            isCorrect = false,
            voteCount = 818,
            ratio = 35.0f,
            stance = "CON"
        )
    )

    val dummyPerspectives = listOf(
        PerspectiveUiModel(
            commentId = "101",
            profileImageUrl = "",
            nickname = "나",
            optionTitle = "민초 극호",
            optionId = 1L,
            content = "솔직히 민초만큼 완벽한 디저트가 어디 있나요? 달콤함과 상쾌함을 동시에 느낄 수 있는 궁극의 맛입니다. 반박 안 받습니다.",
            timeAgo = "방금 전",
            replyCount = 5,
            likeCount = 12,
            isLiked = true,
            isMine = true
        ),
        PerspectiveUiModel(
            commentId = "102",
            profileImageUrl = "",
            nickname = "반민초협회장",
            optionTitle = "민초 극불호",
            optionId = 2L,
            content = "초콜릿에 치약을 섞어 먹는 기분입니다. 돈 주고 사먹는 사람들의 미각이 의심됩니다... 양치를 두 번 하세요 그냥.",
            timeAgo = "10분 전",
            replyCount = 24,
            likeCount = 842,
            isLiked = false,
            isMine = false
        ),
        PerspectiveUiModel(
            commentId = "103",
            profileImageUrl = "",
            nickname = "쩝쩝박사",
            optionTitle = "민초 극호",
            optionId = 1L,
            content = "아이스크림 가게 가면 무조건 파인트 첫 번째 맛은 민트초코칩 고정이지 ㅋㅋㅋ",
            timeAgo = "1시간 전",
            replyCount = 0,
            likeCount = 45,
            isLiked = false,
            isMine = false
        ),
        PerspectiveUiModel(
            commentId = "104",
            profileImageUrl = "",
            nickname = "초코파이",
            optionTitle = "민초 극불호",
            optionId = 2L,
            content = "민초단들은 제발 조용히 해주세요.",
            timeAgo = "3시간 전",
            replyCount = 2,
            likeCount = 15,
            isLiked = true,
            isMine = false
        )
    )

    val dummyRecommends = listOf(
        RecommendUiModel(
            battleId = "rec_001",
            title = "평생 한 가지 음식만 먹어야 한다면?",
            summary = "짜장면 vs 짬뽕, 당신의 소울푸드를 선택해주세요!",
            audioDuration = 145, // 2분 25초
            viewCount = 15420,
            participantsCount = 8900,
            tags = listOf("음식", "취향", "밸런스게임"),
            imageA = "",
            imageB = "",
            stanceA = "짜장면",
            stanceB = "짬뽕",
            representativeA = "윤기 좔좔 간짜장",
            representativeB = "얼큰한 차돌짬뽕"
        ),
        RecommendUiModel(
            battleId = "rec_002",
            title = "태블릿 PC, 정말 필수일까?",
            summary = "생산성 향상을 위한 필수템 vs 스마트폰과 노트북으로 충분하다",
            audioDuration = 340, // 5분 40초
            viewCount = 8230,
            participantsCount = 3120,
            tags = listOf("IT", "전자기기", "소비"),
            imageA = "",
            imageB = "",
            stanceA = "필수템이다",
            stanceB = "사치템이다",
            representativeA = "아이패드 프로",
            representativeB = "스마트폰 & 노트북"
        ),
        RecommendUiModel(
            battleId = "rec_003",
            title = "가장 이상적인 근무 형태는?",
            summary = "출퇴근 시간 아끼는 재택근무 vs 동료들과 소통하는 사무실 출근",
            audioDuration = 275, // 4분 35초
            viewCount = 21050,
            participantsCount = 12500,
            tags = listOf("직장인", "워라밸", "라이프스타일"),
            imageA = "",
            imageB = "",
            stanceA = "풀 재택근무",
            stanceB = "사무실 출근",
            representativeA = "내 방 데스크셋업",
            representativeB = "강남 오피스"
        )
    )

    val dummyScripts = listOf(
        ScenarioScriptUiModel(
            scriptId = "script_001",
            startTimeMs = 0L,
            speakerType = SpeakerType.NARRATOR,
            speakerName = "진행자",
            displayText = "지금부터 '평생 한 가지 음식만 먹어야 한다면?'을 주제로 배틀을 시작하겠습니다.",
            profileImageUrl = null
        ),
        ScenarioScriptUiModel(
            scriptId = "script_002",
            startTimeMs = 4500L,
            speakerType = SpeakerType.A,
            speakerName = "윤기좔좔 짜장파",
            displayText = "당연히 짜장면 아닌가요? 달콤하고 짭짤한 춘장 소스에 단무지 하나 올려 먹으면 매일 먹어도 안 질립니다.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_003",
            startTimeMs = 11000L,
            speakerType = SpeakerType.B,
            speakerName = "얼큰국물 짬뽕파",
            displayText = "비 오는 날 짜장면 드실 겁니까? 짬뽕의 얼큰한 국물과 불맛은 절대 포기할 수 없죠.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_004",
            startTimeMs = 17500L,
            speakerType = SpeakerType.A,
            speakerName = "윤기좔좔 짜장파",
            displayText = "짬뽕은 먹고 나면 옷에 국물 튀어서 불편하기만 합니다.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_005",
            startTimeMs = 21000L,
            speakerType = SpeakerType.USER,
            speakerName = "나",
            displayText = "음... 저는 짬짜면으로 합의 보겠습니다.",
            profileImageUrl = ""
        ),
        ScenarioScriptUiModel(
            scriptId = "script_006",
            startTimeMs = 24500L,
            speakerType = SpeakerType.UNKNOWN,
            speakerName = "익명의 방청객",
            displayText = "(웅성웅성) 짬짜면은 반칙 아닌가요?",
            profileImageUrl = null
        )
    )

    val dummyPastChoices = listOf(
        PastChoice(
            scriptIndex = 5,
            options = listOf(
                ScenarioOptionUiModel(
                    label = "짜장면 측 반론 듣기",
                    nextNodeId = "node_pro_rebuttal"
                ),
                ScenarioOptionUiModel(
                    label = "짬뽕 측 반론 듣기",
                    nextNodeId = "node_con_rebuttal"
                )
            ),
            selectedNextNodeId = "node_con_rebuttal"
        ),
        PastChoice(
            scriptIndex = 12,
            options = listOf(
                ScenarioOptionUiModel(
                    label = "최종 결론 듣기",
                    nextNodeId = "node_conclusion"
                ),
                ScenarioOptionUiModel(
                    label = "전문가 의견 듣기",
                    nextNodeId = "node_expert"
                ),
                ScenarioOptionUiModel(
                    label = "바로 투표하기",
                    nextNodeId = "node_vote"
                )
            ),
            selectedNextNodeId = "node_conclusion"
        )
    )
}