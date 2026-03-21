package com.swyp4.team2.data.mapper

import com.swyp4.team2.data.model.HomeContentItemDto
import com.swyp4.team2.ui.home.model.ContentType
import com.swyp4.team2.ui.home.model.HomeContentModel
import com.swyp4.team2.ui.home.model.TodayPickModel

// 1. 일반 배틀(BATTLE) 변환 매퍼
fun HomeContentItemDto.toHomeContentModel(): HomeContentModel {
    val minutes = (this.audioDuration + 59) / 60

    return HomeContentModel(
        contentId = this.battleId,
        type = runCatching { ContentType.valueOf(this.type) }.getOrDefault(ContentType.UNKNOWN),
        title = this.title,
        summary = this.summary,
        thumbnailUrl = this.thumbnailUrl,
        viewCountText = "%,d".format(this.viewCount),
        timeInfoText = "${minutes}분",
        tags = this.tags,
        leftOpinion = null,
        leftProfileName = null,
        rightOpinion = null,
        rightProfileName = null
    )
}

// 2. 오늘의 Pické (VOTE, QUIZ) 변환 매퍼
fun HomeContentItemDto.toTodayPickModel(): TodayPickModel? {
    val participantsText = "%,d명 참여".format(this.participantsCount)

    return when (this.type) {
        "VOTE" -> TodayPickModel.VotePick(
            contentId = this.battleId,
            title = this.title,
            summary = this.summary,
            participantsText = participantsText,
            leftOptionText = this.options.getOrNull(0)?.text ?: "A",
            rightOptionText = this.options.getOrNull(1)?.text ?: "B"
        )
        "QUIZ" -> TodayPickModel.QuizPick(
            contentId = this.battleId,
            title = this.title,
            summary = this.summary,
            participantsText = participantsText,
            options = this.options.map { it.text }
        )
        else -> null
    }
}