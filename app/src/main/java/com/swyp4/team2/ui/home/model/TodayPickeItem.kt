package com.swyp4.team2.ui.home.model

sealed class TodayPickeItem(
    val id: Int,
    val typeName: String,
    val participants: String
) {
    // 1. A vs B 선택형
    data class AbType(
        val id_ab: Int,
        val type_ab: String,
        val participants_ab: String,
        val title: String,
        val description: String,
        val leftOption: String,
        val leftSub: String,
        val rightOption: String,
        val rightSub: String
    ) : TodayPickeItem(id_ab, type_ab, participants_ab)

    // 2. 4지 선다형
    data class SelectionType(
        val id_sel: Int,
        val type_sel: String,
        val participants_sel: String,
        val titlePart1: String, // "도덕의 기준은 "
        val titlePart2: String, // " 이다"
        val description: String,
        val options: List<String> // ["결과", "의도", "규칙", "덕"]
    ) : TodayPickeItem(id_sel, type_sel, participants_sel)
}

// 🌟 더미 데이터 리스트
val dummyPickeList = listOf(
    TodayPickeItem.AbType(
        1, "퀴즈", "1,340명 참여",
        "AI가 만든 그림도 '예술 작품'으로 인정해야 할까?",
        "인간의 창의성 없이 생성된 결과물도 예술로 볼 수 있을까요?\n지금 바로 당신의 입장을 선택하세요",
        "당연하다", "도구가 달라졌을 뿐",
        "인정 못 한다", "창작 의도가 없다"
    ),
    TodayPickeItem.SelectionType(
        2, "투표", "985명 참여",
        "도덕의 기준은 ", " 이다",
        "빈칸에 들어갈 가장 적절한 답을 골라주세요",
        listOf("결과", "의도", "규칙", "덕")
    )
)
