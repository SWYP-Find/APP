package com.picke.app.data.model

import com.picke.app.domain.model.BattleDetailBoard
import com.picke.app.domain.model.BattleInfoBoard
import com.picke.app.domain.model.BattleOptionBoard
import com.picke.app.domain.model.BattleTagBoard

data class BattleDetailDto(
    val battleInfo: BattleInfoDto?,
    val titlePrefix: String?,
    val titleSuffix: String?,
    val itemA: String?,
    val itemADesc: String?,
    val itemB: String?,
    val itemBDesc: String?,
    val description: String?,
    val shareUrl: String?,
    val userVoteStatus: String?,
    val categoryTags: List<BattleTagDto>?,
    val philosopherTags: List<BattleTagDto>?,
    val valueTags: List<BattleTagDto>?
)

data class BattleInfoDto(
    val battleId: Long?,
    val title: String?,
    val summary: String?,
    val thumbnailUrl: String?,
    val type: String?,
    val viewCount: Int?,
    val participantsCount: Int?,
    val audioDuration: Int?,
    val tags: List<BattleTagDto>?,
    val options: List<BattleOptionDto>?
)

data class BattleOptionDto(
    val optionId: Long?,
    val label: String?,
    val title: String?,
    val stance: String?,
    val representative: String?,
    val quote: String?,
    val imageUrl: String?,
    val tags: List<BattleTagDto>?
)

data class BattleTagDto(
    val tagId: Long?,
    val name: String?,
    val type: String?
)

// Data -> Domain
fun BattleDetailDto.toDomainModel() = BattleDetailBoard(
    battleInfo = this.battleInfo?.toDomainModel() ?: BattleInfoBoard.empty(),
    titlePrefix = this.titlePrefix ?: "",
    titleSuffix = this.titleSuffix ?: "",
    itemA = this.itemA ?: "",
    itemADesc = this.itemADesc ?: "",
    itemB = this.itemB ?: "",
    itemBDesc = this.itemBDesc ?: "",
    description = this.description ?: "",
    shareUrl = this.shareUrl ?: "",
    userVoteStatus = this.userVoteStatus ?: "NONE",
    categoryTags = this.categoryTags?.map { it.toDomainModel() } ?: emptyList(),
    philosopherTags = this.philosopherTags?.map { it.toDomainModel() } ?: emptyList(),
    valueTags = this.valueTags?.map { it.toDomainModel() } ?: emptyList()
)

fun BattleInfoDto.toDomainModel() = BattleInfoBoard(
    battleId = this.battleId.toString() ?: "1",
    title = this.title ?: "",
    summary = this.summary ?: "",
    thumbnailUrl = this.thumbnailUrl ?: "",
    type = this.type ?: "BATTLE",
    viewCount = this.viewCount ?: 0,
    participantsCount = this.participantsCount ?: 0,
    audioDuration = this.audioDuration ?: 0,
    tags = this.tags?.map { it.toDomainModel() } ?: emptyList(),
    options = this.options?.map { it.toDomainModel() } ?: emptyList()
)

fun BattleOptionDto.toDomainModel() = BattleOptionBoard(
    optionId = this.optionId.toString() ?: "1",
    label = this.label ?: "",
    title = this.title ?: "",
    stance = this.stance ?: "",
    representative = this.representative ?: "",
    quote = this.quote ?: "",
    imageUrl = this.imageUrl ?: "",
    tags = this.tags?.map { it.toDomainModel() } ?: emptyList()
)

fun BattleTagDto.toDomainModel() = BattleTagBoard(
    tagId = this.tagId.toString() ?: "",
    name = this.name ?: "",
    type = this.type ?: ""
)
