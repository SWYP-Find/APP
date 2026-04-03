package com.picke.app.ui

import androidx.annotation.DrawableRes
import com.picke.app.R

enum class PhilosopherType(
    val title: String,
    val description: String,
    val hashtags: List<String>,
    @DrawableRes val imageRes: Int // 🌟 안드로이드 로컬 이미지 리소스 아이디
) {
    SOCRATES(
        title = "소크라테스",
        description = "끊임없이 질문하고 본질을 탐구하는 지혜로운 철학자입니다.",
        hashtags = listOf("문답법", "지혜", "너자신을알라"),
        imageRes = R.drawable.ic_profile_mengzi
    ),
    PLATO(
        title = "플라톤",
        description = "눈에 보이지 않는 이상적인 세계, 이데아를 꿈꿉니다.",
        hashtags = listOf("이데아", "철인정치", "동굴의비유"),
        imageRes = R.drawable.ic_profile_mengzi
    ),
    MARX(
        title = "마르크스",
        description = "사회의 구조적 모순을 꿰뚫어 보고 변화를 촉구합니다.",
        hashtags = listOf("자본론", "계급투쟁", "혁명"),
        imageRes = R.drawable.ic_profile_mengzi
    ),
    ROUSSEAU(
        title = "루소",
        description = "인간은 본래 선하며, 자연으로 돌아가야 한다고 믿습니다.",
        hashtags = listOf("성선설", "자연상태", "사회계약론"),
        imageRes = R.drawable.ic_profile_mengzi
    ),
    HOBBES(
        title = "홉스",
        description = "질서를 위해 강력한 국가(리바이어던)가 필요하다고 주장합니다.",
        hashtags = listOf("성악설", "만인의투쟁", "리바이어던"),
        imageRes = R.drawable.ic_profile_mengzi
    ),
    UNKNOWN(
        title = "알 수 없는 철학자",
        description = "아직 자아를 찾는 중입니다.",
        hashtags = emptyList(),
        imageRes = R.drawable.ic_launcher_foreground
    );

    companion object {
        /**
         * 🌟 백엔드에서 받은 문자열(ex: "SOCRATES")을 안전하게 Enum으로 바꿔주는 마법의 함수!
         * 일치하는 게 없으면 앱이 죽지 않고 UNKNOWN을 반환합니다.
         */
        fun fromString(type: String?): PhilosopherType {
            if (type.isNullOrBlank()) return UNKNOWN
            return entries.find { it.name.equals(type, ignoreCase = true) } ?: UNKNOWN
        }
    }
}