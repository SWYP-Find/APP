package com.swyp4.team2.ui.debate

import com.swyp4.team2.domain.model.DebateMessage
import com.swyp4.team2.domain.model.SpeakerType

enum class SpeakerTypeLocal {
    LEFT,  // 왼쪽 화자 (예: 플라톤)
    RIGHT  // 오른쪽 화자 (예: 사르트르)
}

data class DebateMessageLocal(
    val id: Int,
    val timeMs: Long,
    val speakerType: SpeakerTypeLocal,
    val speakerName: String,
    val profileRes: Int, // 프로필 이미지 리소스
    val text: String
)

object DebateDummyData {
    // 🌟 피그마 이미지와 동일한 대사 및 구조로 업데이트
    val debateScripts = listOf(
        DebateMessageLocal(1, 0, SpeakerTypeLocal.LEFT, "플라톤", com.swyp4.team2.R.drawable.ic_profile_xunzi, "이건 기만입니다. 하늘 아래 모든 사물은 그에 걸맞은 완벽한 목적과 형상, 즉 '이데아'를 가지고 있습니다."),
        DebateMessageLocal(2, 5000, SpeakerTypeLocal.LEFT, "플라톤", com.swyp4.team2.R.drawable.ic_profile_mengzi, "변기의 이데아는 '배설물을 처리하는 것'이지, 감상하는 것이 아닙니다."),
        DebateMessageLocal(3, 10000, SpeakerTypeLocal.LEFT, "플라톤", com.swyp4.team2.R.drawable.ic_profile_xunzi, "사물의 본질을 왜곡하여 대중을 혼란에 빠뜨리는 것은 진리를 모독하는 행위입니다."),

        DebateMessageLocal(4, 16000, SpeakerTypeLocal.RIGHT, "사르트르", com.swyp4.team2.R.drawable.ic_profile_mengzi, "플라톤 선생님, 당신은 사물에 '영혼'이 미리 정해져 있다고 믿는군요. 하지만 사물은 그저 그곳에 존재할 뿐입니다."),
        DebateMessageLocal(5, 23000, SpeakerTypeLocal.RIGHT, "사르트르", com.swyp4.team2.R.drawable.ic_profile_xunzi, "인간이 그것을 어떻게 사용하고 어떤 의미를 부여하느냐에 따라 본질은 언제든 바뀔 수 있습니다."),
        DebateMessageLocal(6, 30000, SpeakerTypeLocal.RIGHT, "사르트르", com.swyp4.team2.R.drawable.ic_profile_mengzi, "뒤샹이 이 물건을 '샘'이라고 부르기로 선택한 순간, 이 물체의 본질은 배설 도구에서 예술 작품으로 재탄생한 것입니다. 실존은 본질에 앞서니까요."),

        DebateMessageLocal(7, 40000, SpeakerTypeLocal.LEFT, "플라톤", com.swyp4.team2.R.drawable.ic_profile_xunzi, "예술이란 이데아를 모방하려는 숭고한 노력입니다. 화가는 붓질을 통해, 조각가는 망치질을 통해 그 본질에 가까워지려 애쓰죠.")
    )
}