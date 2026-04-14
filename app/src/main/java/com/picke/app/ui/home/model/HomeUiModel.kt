package com.picke.app.ui.home.model

import com.picke.app.domain.model.HomeBoard
import com.picke.app.domain.model.HomeContent
import com.picke.app.domain.model.PollQuizOptionStatBoard
import com.picke.app.domain.model.TodayPick

enum class ContentUiType {
    BATTLE, VOTE, QUIZ, UNKNOWN
}

data class HomeBoardUiModel(
    val hasNewNotice: Boolean,
    val editorPicks: List<HomeContentUiModel>,
    val trendingBattles: List<HomeContentUiModel>,
    val bestBattles: List<HomeContentUiModel>,
    val todayPicks: List<TodayPickUiModel>,
    val newBattles: List<HomeContentUiModel>
)

data class HomeContentUiModel(
    val contentId: String,
    val type: ContentUiType,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val viewCountText: String,
    val timeInfoText: String,
    val tags: List<String>,
    val leftOpinion: String? = null,
    val leftProfileName: String? = null,
    val leftProfileImageUrl: String? = null,
    val rightOpinion: String? = null,
    val rightProfileName: String? = null,
    val rightProfileImageUrl: String? = null
)

// 추가된 옵션 상태 UI 모델
data class PollQuizOptionStatUiModel(
    val optionId: Long,
    val label: String,
    val title: String,
    val isCorrect: Boolean,
    val stance: String,
    val voteCount: Int,
    val ratio: Float
)

// 수정된 TodayPickUiModel
sealed class TodayPickUiModel {
    abstract val contentId: String
    abstract val title: String
    abstract val summary: String
    abstract val participantsCount: Int
    abstract val selectedOptionId: Long?
    abstract val options: List<PollQuizOptionStatUiModel>
    abstract val type: String

    data class VotePick(
        override val contentId: String,
        override val title: String,
        val titlePrefix: String,
        val titleSuffix: String,
        override val summary: String,
        override val participantsCount: Int,
        override val selectedOptionId: Long?,
        override val options: List<PollQuizOptionStatUiModel>,
        override val type: String
    ) : TodayPickUiModel()

    data class QuizPick(
        override val contentId: String,
        override val title: String,
        override val summary: String,
        override val participantsCount: Int,
        override val selectedOptionId: Long?,
        override val options: List<PollQuizOptionStatUiModel>,
        override val type: String
    ) : TodayPickUiModel()
}

// Mappers
fun HomeBoard.toUiModel(): HomeBoardUiModel {
    return HomeBoardUiModel(
        hasNewNotice = this.hasNewNotice,
        editorPicks = this.editorPicks.map { it.toUiModel() },
        trendingBattles = this.trendingBattles.map { it.toUiModel() },
        bestBattles = this.bestBattles.map { it.toUiModel() },
        todayPicks = this.todayPicks.map { it.toUiModel() },
        newBattles = this.newBattles.map { it.toUiModel() }
    )
}

fun HomeContent.toUiModel(): HomeContentUiModel {
    return HomeContentUiModel(
        contentId = this.contentId,
        title = this.title,
        summary = this.summary,
        thumbnailUrl = this.thumbnailUrl,
        viewCountText = this.viewCount.toString(),
        timeInfoText = "${this.audioDuration / 60}분",
        tags = this.tags,
        leftOpinion = this.options.getOrNull(0)?.text ?: "",
        leftProfileName = this.options.getOrNull(0)?.philosopherName ?: "알 수 없음",
        leftProfileImageUrl = this.options.getOrNull(0)?.imageUrl ?: "",
        rightOpinion = this.options.getOrNull(1)?.text ?: "",
        rightProfileName = this.options.getOrNull(1)?.philosopherName ?: "알 수 없음",
        rightProfileImageUrl = this.options.getOrNull(1)?.imageUrl ?: "",
        type = when (this.type) {
            com.picke.app.domain.model.ContentDomainType.BATTLE -> ContentUiType.BATTLE
            com.picke.app.domain.model.ContentDomainType.VOTE -> ContentUiType.VOTE
            com.picke.app.domain.model.ContentDomainType.QUIZ -> ContentUiType.QUIZ
            else -> ContentUiType.UNKNOWN
        }
    )
}

fun PollQuizOptionStatBoard.toUiModel() = PollQuizOptionStatUiModel(
    optionId = this.optionId,
    label = this.label,
    title = this.title,
    isCorrect = this.isCorrect,
    voteCount = this.voteCount,
    ratio = this.ratio,
    stance = this.stance
)

fun TodayPick.toUiModel(): TodayPickUiModel {
    return when (this) {
        is TodayPick.VotePick -> TodayPickUiModel.VotePick(
            contentId = this.contentId,
            title = this.title,
            titlePrefix = this.titlePrefix,
            titleSuffix = this.titleSuffix,
            summary = this.summary,
            participantsCount = this.participantsCount,
            selectedOptionId = this.selectedOptionId,
            options = this.options.map { it.toUiModel() },
            type = this.type
        )
        is TodayPick.QuizPick -> TodayPickUiModel.QuizPick(
            contentId = this.contentId,
            title = this.title,
            summary = this.summary,
            participantsCount = this.participantsCount,
            selectedOptionId = this.selectedOptionId,
            options = this.options.map { it.toUiModel() },
            type = this.type
        )
    }
}