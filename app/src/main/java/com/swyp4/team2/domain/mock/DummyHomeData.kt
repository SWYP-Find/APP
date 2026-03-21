package com.swyp4.team2.domain.mock

import com.swyp4.team2.domain.model.HomeBoardData
import com.swyp4.team2.ui.home.model.ContentType
import com.swyp4.team2.ui.home.model.HomeContentModel
import com.swyp4.team2.ui.home.model.TodayPickModel

object DummyHomeData {

    private val dummyEditorPicks = listOf(
        HomeContentModel(
            contentId = "1",
            type = ContentType.BATTLE,
            title = "뒤샹의 변기, 예술인가 도발인가",
            summary = "뒤샹의 변기 <샘>은 “무엇이 예술인가”를 묻는 작품이다.",
            thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
            viewCountText = "847",
            timeInfoText = "3분",
            tags = listOf("예술", "현대미술"),
            leftOpinion = "예술이다",
            rightOpinion = "쓰레기다"
        )
    )

    private val dummyTrendingBattles = listOf(
        HomeContentModel(
            contentId = "2",
            type = ContentType.BATTLE,
            title = "인간은 본래 선한가, 악한가?",
            summary = "최근 24시간 참여가 급증한 배틀",
            thumbnailUrl = "https://picsum.photos/200/150",
            viewCountText = "1,340",
            timeInfoText = "8분",
            tags = listOf("철학")
        ),
        HomeContentModel(
            contentId = "3",
            type = ContentType.BATTLE,
            title = "안락사 도입, 당신의 선택은?",
            summary = "최근 24시간 참여가 급증한 배틀",
            thumbnailUrl = "https://picsum.photos/201/150",
            viewCountText = "1,132",
            timeInfoText = "5분",
            tags = listOf("역사")
        )
    )

    private val dummyBestBattles = listOf(
        HomeContentModel(
            contentId = "4",
            type = ContentType.BATTLE,
            title = "인간은 본래 선한가, 악한가?",
            summary = "누적 참여와 댓글 반응이 높은 배틀",
            thumbnailUrl = "",
            viewCountText = "1,340",
            timeInfoText = "8분",
            tags = listOf("철학", "인문"),
            leftProfileName = "맹자",
            rightProfileName = "순자"
        ),
        HomeContentModel(
            contentId = "5",
            type = ContentType.BATTLE,
            title = "죽음을 앞둔 사람에게 진실을 말해야 하는가?",
            summary = "누적 참여와 댓글 반응이 높은 배틀",
            thumbnailUrl = "",
            viewCountText = "1,340",
            timeInfoText = "8분",
            tags = listOf("철학", "인문"),
            leftProfileName = "칸트",
            rightProfileName = "톨스토이"
        )
    )

    private val dummyTodayPicks = listOf(
        TodayPickModel.QuizPick(
            contentId = "6",
            title = "AI가 만든 그림도 '예술 작품'으로 인정해야 할까?",
            summary = "인간의 창의성 없이 생성된 결과물도 예술로 볼 수 있을까요?\n지금 바로 당신의 입장을 선택하세요",
            participantsText = "1,340명 참여",
            options = listOf("당연하다", "인정 못 한다")
        ),
        TodayPickModel.VotePick(
            contentId = "7",
            title = "도덕의 기준은 [   ] 이다",
            summary = "빈칸에 들어갈 가장 적절한 답을 골라주세요",
            participantsText = "985명 참여",
            leftOptionText = "결과",
            rightOptionText = "의도"
        )
    )

    private val dummyNewBattles = listOf(
        HomeContentModel(
            contentId = "8",
            type = ContentType.BATTLE,
            title = "노키즈존: 영업상의 자유인가, 차별인가?",
            summary = "옆 테이블 아이의 울음소리가 평화로운 휴식시간을 깨뜨린다면?",
            thumbnailUrl = "",
            viewCountText = "726",
            timeInfoText = "5분",
            tags = listOf("사회"),
            leftOpinion = "영업상 자유",
            leftProfileName = "주인",
            rightOpinion = "명백한 차별",
            rightProfileName = "손님"
        )
    )

    val getDummyBoardData = HomeBoardData(
        hasNewNotice = true,
        editorPicks = dummyEditorPicks,
        trendingBattles = dummyTrendingBattles,
        bestBattles = dummyBestBattles,
        todayPicks = dummyTodayPicks,
        newBattles = dummyNewBattles
    )
}