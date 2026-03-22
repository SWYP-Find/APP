package com.swyp4.team2.domain.mock

// 🌟 UI 모델이나 DTO 임포트가 싹 사라지고, 오직 Domain 모델만 임포트합니다!
import com.swyp4.team2.domain.model.ContentDomainType
import com.swyp4.team2.domain.model.ContentOption
import com.swyp4.team2.domain.model.HomeBoard
import com.swyp4.team2.domain.model.HomeContent
import com.swyp4.team2.domain.model.TodayPick

object DummyHomeData {

    private val dummyEditorPicks = listOf(
        HomeContent(
            contentId = "1",
            type = ContentDomainType.BATTLE,
            title = "뒤샹의 변기, 예술인가 도발인가",
            summary = "뒤샹의 변기 <샘>은 “무엇이 예술인가”를 묻는 작품이다.",
            thumbnailUrl = "https://images.unsplash.com/photo-1618005182384-a83a8bd57fbe?q=80&w=1000&auto=format&fit=crop",
            viewCount = 847,      // 🌟 "847" -> 847 (순수 숫자)
            audioDuration = 180,  // 🌟 "3분" -> 180 (초 단위 순수 숫자)
            tags = listOf("예술", "현대미술"),
            // 🌟 left/right 텍스트 대신, 서버 스펙에 맞춘 options 배열 사용
            options = listOf(
                ContentOption(label = "A", text = "예술이다", imageUrl = null),
                ContentOption(label = "B", text = "쓰레기다", imageUrl = null)
            )
        )
    )

    private val dummyTrendingBattles = listOf(
        HomeContent(
            contentId = "2",
            type = ContentDomainType.BATTLE,
            title = "인간은 본래 선한가, 악한가?",
            summary = "최근 24시간 참여가 급증한 배틀",
            thumbnailUrl = "https://picsum.photos/200/150",
            viewCount = 1340,     // 🌟 "1,340" -> 1340
            audioDuration = 480,  // 🌟 "8분" -> 480초
            tags = listOf("철학"),
            options = emptyList()
        ),
        HomeContent(
            contentId = "3",
            type = ContentDomainType.BATTLE,
            title = "안락사 도입, 당신의 선택은?",
            summary = "최근 24시간 참여가 급증한 배틀",
            thumbnailUrl = "https://picsum.photos/201/150",
            viewCount = 1132,
            audioDuration = 300,  // 🌟 "5분" -> 300초
            tags = listOf("역사"),
            options = emptyList()
        )
    )

    private val dummyBestBattles = listOf(
        HomeContent(
            contentId = "4",
            type = ContentDomainType.BATTLE,
            title = "인간은 본래 선한가, 악한가?",
            summary = "누적 참여와 댓글 반응이 높은 배틀",
            thumbnailUrl = "",
            viewCount = 1340,
            audioDuration = 480,
            tags = listOf("철학", "인문"),
            options = listOf(
                ContentOption(label = "맹자", text = "성선설", imageUrl = null),
                ContentOption(label = "순자", text = "성악설", imageUrl = null)
            )
        ),
        HomeContent(
            contentId = "5",
            type = ContentDomainType.BATTLE,
            title = "죽음을 앞둔 사람에게 진실을 말해야 하는가?",
            summary = "누적 참여와 댓글 반응이 높은 배틀",
            thumbnailUrl = "",
            viewCount = 1340,
            audioDuration = 480,
            tags = listOf("철학", "인문"),
            options = listOf(
                ContentOption(label = "칸트", text = "말해야 한다", imageUrl = null),
                ContentOption(label = "톨스토이", text = "숨겨야 한다", imageUrl = null)
            )
        )
    )

    private val dummyTodayPicks = listOf(
        TodayPick.QuizPick(
            contentId = "6",
            title = "AI가 만든 그림도 '예술 작품'으로 인정해야 할까?",
            summary = "인간의 창의성 없이 생성된 결과물도 예술로 볼 수 있을까요?\n지금 바로 당신의 입장을 선택하세요",
            participantsCount = 1340, // 🌟 "1,340명 참여" -> 1340 (순수 숫자)
            options = listOf("당연하다", "인정 못 한다")
        ),
        TodayPick.VotePick(
            contentId = "7",
            title = "도덕의 기준은 [   ] 이다",
            summary = "빈칸에 들어갈 가장 적절한 답을 골라주세요",
            participantsCount = 985, // 🌟 "985명 참여" -> 985 (순수 숫자)
            leftOptionText = "결과",
            rightOptionText = "의도"
        )
    )

    private val dummyNewBattles = listOf(
        HomeContent(
            contentId = "8",
            type = ContentDomainType.BATTLE,
            title = "노키즈존: 영업상의 자유인가, 차별인가?",
            summary = "옆 테이블 아이의 울음소리가 평화로운 휴식시간을 깨뜨린다면?",
            thumbnailUrl = "",
            viewCount = 726,
            audioDuration = 300,
            tags = listOf("사회"),
            options = listOf(
                ContentOption(label = "주인", text = "영업상 자유", imageUrl = null),
                ContentOption(label = "손님", text = "명백한 차별", imageUrl = null)
            )
        )
    )

    // 🌟 최종 반환 타입은 앱의 심장인 'HomeBoard' 입니다!
    val getDummyBoardData = HomeBoard(
        hasNewNotice = true,
        editorPicks = dummyEditorPicks,
        trendingBattles = dummyTrendingBattles,
        bestBattles = dummyBestBattles,
        todayPicks = dummyTodayPicks,
        newBattles = dummyNewBattles
    )
}