package com.picke.presentation.ui.scenario.model

data class ScenarioUiState(
    val title: String = "",
    val pastScripts: List<ScenarioScriptUiModel> = emptyList(),
    val pastChoices: List<PastChoice> = emptyList(),
    val scripts: List<ScenarioScriptUiModel> = emptyList(),
    val activeIndex: Int = -1,
    val maxRevealedIndex: Int = -1,
    val nodeEndTimeMs: Long = 0L,
    val isPlaying: Boolean = false,
    val currentPositionMs: Long = 0L,
    val maxListenedPositionMs: Long = 0L,
    val totalDurationMs: Long = 0L,
    val interactiveOptions: List<ScenarioOptionUiModel> = emptyList(),
    val currentNodeId: String = "",
    val showOptions: Boolean = false,
    val showFinalVoteDialog: Boolean = false,
    val isReviewing: Boolean = false
)