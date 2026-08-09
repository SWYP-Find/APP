package com.picke.presentation.ui.scenario.model

data class PastChoice(
    val scriptIndex: Int,
    val options: List<ScenarioOptionUiModel>,
    val selectedNextNodeId: String
)