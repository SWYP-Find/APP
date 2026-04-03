package com.picke.app.ui.home.model

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

