package com.picke.app.domain.model

data class BattleDetailBoard(
    val battleInfo: BattleInfoBoard,
    val titlePrefix: String,
    val titleSuffix: String,
    val itemA: String,
    val itemADesc: String,
    val itemB: String,
    val itemBDesc: String,
    val description: String,
    val shareUrl: String,
    val userVoteStatus: String,
    val categoryTags: List<BattleTagBoard>,
    val philosopherTags: List<BattleTagBoard>,
    val valueTags: List<BattleTagBoard>
)

data class BattleInfoBoard(
    val battleId: String,
    val title: String,
    val summary: String,
    val thumbnailUrl: String,
    val type: String,
    val viewCount: Int,
    val participantsCount: Int,
    val audioDuration: Int,
    val tags: List<BattleTagBoard>,
    val options: List<BattleOptionBoard>
){
    companion object {
        fun empty() = BattleInfoBoard(
            battleId = "1",
            title = "",
            summary = "",
            thumbnailUrl = "",
            type = "BATTLE",
            viewCount = 0,
            participantsCount = 0,
            audioDuration = 0,
            tags = emptyList(),
            options = emptyList()
        )
    }
}

data class BattleOptionBoard(
    val optionId: String,
    val label: String,
    val title: String,
    val stance: String,
    val representative: String,
    val quote: String,
    val imageUrl: String,
    val tags: List<BattleTagBoard>
)

data class BattleTagBoard(
    val tagId: String,
    val name: String,
    val type: String
)