package com.picke.presentation.ui.vote.model

import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.model.BattleInfoBoard
import com.picke.domain.feature.battle.model.BattleOptionBoard
import com.picke.domain.feature.battle.model.BattleTagBoard

data class BattleDetailUiModel(
    val battleInfo: BattleInfoUiModel,
    val description: String,
    val shareUrl: String,
    val userVoteStatus: String,
    val currentStep: String,
    val categoryTags: List<BattleTagUiModel>,
    val philosopherTags: List<BattleTagUiModel>,
    val valueTags: List<BattleTagUiModel>
)

data class BattleInfoUiModel(
    val battleId: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val viewCount: Int,
    val participantsCount: Int,
    val audioDuration: Int,
    val tags: List<BattleTagBoard>,
    val options: List<BattleOptionUiModel>
)

data class BattleOptionUiModel(
    val optionId: String,
    val title: String,
    val stance: String,
    val representative: String,
    val imageUrl: String,
    val tags: List<BattleTagUiModel>
)

data class BattleTagUiModel(
    val tagId: String,
    val name: String,
    val type: String
)

fun BattleDetailBoard.toUiModel() = BattleDetailUiModel(
    battleInfo = battleInfo.toUiModel(),
    description = description,
    shareUrl = shareUrl,
    userVoteStatus = userVoteStatus,
    currentStep = currentStep,
    categoryTags = categoryTags.map { it.toUiModel() },
    philosopherTags = philosopherTags.map { it.toUiModel() },
    valueTags = valueTags.map { it.toUiModel() }
)

fun BattleInfoBoard.toUiModel() = BattleInfoUiModel(
    battleId = battleId,
    title = title,
    summary = summary,
    thumbnailUrl = thumbnailUrl,
    viewCount = viewCount,
    participantsCount = participantsCount,
    audioDuration = audioDuration,
    tags = tags,
    options = options.map { it.toUiModel() }
)

fun BattleOptionBoard.toUiModel() = BattleOptionUiModel(
    optionId = optionId,
    title = title,
    stance = stance,
    representative = representative,
    imageUrl = imageUrl,
    tags = tags.map { it.toUiModel() }
)

fun BattleTagBoard.toUiModel() = BattleTagUiModel(
    tagId = tagId,
    name = name,
    type = type
)