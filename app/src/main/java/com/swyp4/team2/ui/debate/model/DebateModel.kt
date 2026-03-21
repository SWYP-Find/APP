package com.swyp4.team2.ui.debate // 패키지명은 프로젝트에 맞게 확인해주세요!

import com.swyp4.team2.R

// 🌟 1. 발화자 타입
enum class SpeakerTypeLocal {
    LEFT,   // A, B (철학자)
    RIGHT,  // USER (유저 본인)
    CENTER  // NARRATOR (나레이터)
}

// 🌟 2. 단일 스크립트 (API 명세서 맞춤)
data class DebateMessageLocal(
    val scriptId: String,       // Int -> String으로 변경됨 (API 명세서 uuid)
    val text: String,
    val speakerName: String,
    val speakerType: SpeakerTypeLocal,
    val profileRes: Int,
    val startTimeMs: Int        // timeMs -> startTimeMs로 이름 변경됨
)

// 🌟 3. 선택지 정보 (API 명세서 맞춤)
data class InteractiveOption(
    val label: String,
    val text: String,
    val nextNodeId: String
)

// 🌟 4. 전체 화면 상태를 담는 State
data class DebateUiState(
    val title: String = "",
    val scripts: List<DebateMessageLocal> = emptyList(),
    val activeIndex: Int = -1,
    val isPlaying: Boolean = false,
    val currentPositionMs: Int = 0,
    val totalDurationMs: Int = 0,
    val interactiveOptions: List<InteractiveOption> = emptyList()
)

// 🌟 5. UI 테스트를 위한 더미 데이터 (새로운 모델에 맞게 싹 고쳤습니다!)
object DebateDummyData {
    val debateScripts = listOf(
        DebateMessageLocal("1", "이건 기만입니다. 하늘 아래 모든 사물은 그에 걸맞은 완벽한 목적과 형상, 즉 '이데아'를 가지고 있습니다.", "플라톤", SpeakerTypeLocal.LEFT, R.drawable.ic_profile_xunzi, 0),
        DebateMessageLocal("2", "변기의 이데아는 '배설물을 처리하는 것'이지, 감상하는 것이 아닙니다.", "플라톤", SpeakerTypeLocal.LEFT, R.drawable.ic_profile_mengzi, 5000),
        DebateMessageLocal("3", "사물의 본질을 왜곡하여 대중을 혼란에 빠뜨리는 것은 진리를 모독하는 행위입니다.", "플라톤", SpeakerTypeLocal.LEFT, R.drawable.ic_profile_xunzi, 10000),

        DebateMessageLocal("4", "플라톤 선생님, 당신은 사물에 '영혼'이 미리 정해져 있다고 믿는군요. 하지만 사물은 그저 그곳에 존재할 뿐입니다.", "사르트르", SpeakerTypeLocal.RIGHT, R.drawable.ic_profile_mengzi, 16000),
        DebateMessageLocal("5", "인간이 그것을 어떻게 사용하고 어떤 의미를 부여하느냐에 따라 본질은 언제든 바뀔 수 있습니다.", "사르트르", SpeakerTypeLocal.RIGHT, R.drawable.ic_profile_xunzi, 23000),
        DebateMessageLocal("6", "뒤샹이 이 물건을 '샘'이라고 부르기로 선택한 순간, 이 물체의 본질은 배설 도구에서 예술 작품으로 재탄생한 것입니다. 실존은 본질에 앞서니까요.", "사르트르", SpeakerTypeLocal.RIGHT, R.drawable.ic_profile_mengzi, 30000),

        DebateMessageLocal("7", "예술이란 이데아를 모방하려는 숭고한 노력입니다. 화가는 붓질을 통해, 조각가는 망치질을 통해 그 본질에 가까워지려 애쓰죠.", "플라톤", SpeakerTypeLocal.LEFT, R.drawable.ic_profile_xunzi, 40000)
    )

    val dummyOptions = listOf(
        InteractiveOption("A", "시장은 개인의 욕망이 만나는 곳이며, 자발적 거래라면 그 값은 자유롭게 매겨질 수 있습니다.", "node_a"),
        InteractiveOption("B", "시장은 개인의 욕망이 만나는 곳이며, 자발적 거래라면 그 값은 자유롭게 매겨질 수 있습니다.", "node_b")
    )
}