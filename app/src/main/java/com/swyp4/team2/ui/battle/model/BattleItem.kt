package com.swyp4.team2.ui.battle.model

import com.swyp4.team2.R

data class BattleIntroItem(
    val id: Int,
    val bgImageRes: Int,
    val tags: List<String>,
    val title: String,
    val description: String,
    val timeLeft: String,
    val leftName: String,
    val leftOpinion: String,
    val leftQuote: String,
    val rightName: String,
    val rightOpinion: String,
    val rightQuote: String
)


val dummyBattleIntroList = listOf(
    BattleIntroItem(
        id = 1, bgImageRes = R.drawable.bg_rose,
        tags = listOf("과학", "윤리학"),
        title = "안락사 도입,\n당신의 생각은?",
        description = "인간에게 품위 있는 죽음을 선택할 권리가 있을까요?",
        timeLeft = "4분 30초",
        leftName = "피터 싱어", leftOpinion = "찬성한다", leftQuote = "고통을 끝낼 권리는 본인에게 있다",
        rightName = "임마누엘 칸트", rightOpinion = "반대한다", rightQuote = "생명은 인간이 임의로 결정할 영역이 아니다"
    ),
    BattleIntroItem(
        id = 2, bgImageRes = R.drawable.bg_rose,
        tags = listOf("사회", "도덕"),
        title = "선의의 거짓말,\n정당화 될 수 있는가?",
        description = "결과가 좋다면 거짓말도 선이 될 수 있을까요?",
        timeLeft = "12분 10초",
        leftName = "제러미 벤담", leftOpinion = "정당하다", leftQuote = "최대 다수의 최대 행복이 기준이다",
        rightName = "임마누엘 칸트", rightOpinion = "부당하다", rightQuote = "거짓말은 그 자체로 도덕 법칙 위반이다"
    ),
    BattleIntroItem(id = 3, bgImageRes = R.drawable.bg_rose, tags = listOf("철학"), title = "테스트 3", description = "설명", timeLeft = "1분", leftName = "A", leftOpinion = "찬성", leftQuote = "가", rightName = "B", rightOpinion = "반대", rightQuote = "나"),
    BattleIntroItem(id = 4, bgImageRes = R.drawable.bg_rose, tags = listOf("철학"), title = "테스트 4", description = "설명", timeLeft = "1분", leftName = "A", leftOpinion = "찬성", leftQuote = "가", rightName = "B", rightOpinion = "반대", rightQuote = "나")
)
