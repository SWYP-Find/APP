package com.picke.presentation.ui.perspective.model

import com.picke.domain.feature.perspective.model.PerspectiveDetailBoard
import com.picke.domain.feature.vote.model.VoteStatsOptionBoard

data class PerspectiveUiState(
    val battleId: String = "",
    val voteOptions: List<VoteStatsOptionBoard> = emptyList(),
    val perspectives: List<PerspectiveUiModel> = emptyList(),
    val myPerspective: PerspectiveDetailBoard? = null,
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false,
    val sort: String = "popular",
    val selectedOptionId: Long? = null,
    val opinionChanged: Boolean = false,
    val editingPerspectiveId: Long? = null,
    val battleTitle: String = ""
)