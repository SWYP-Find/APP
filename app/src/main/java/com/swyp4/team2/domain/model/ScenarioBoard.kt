package com.swyp4.team2.domain.model

enum class SpeakerType {
    NARRATOR, A, B, USER, UNKNOWN
}

data class ScenarioBoard(
    val battleId: String,
    val isInteractive: Boolean,
    val startNodeId: String,
    val recommendedPathKey: String,
    val audios: Map<String, String>,
    val nodes: List<ScenarioNode>
)

data class ScenarioNode(
    val nodeId: String,
    val nodeName: String,
    val audioDuration: Int,
    val autoNextNodeId: String?,
    val scripts: List<ScenarioScript>,
    val interactiveOptions: List<ScenarioOption>
)

data class ScenarioScript(
    val scriptId: String,
    val startTimeMs: Long,
    val speakerType: SpeakerType,
    val speakerName: String,
    val text: String
)

data class ScenarioOption(
    val label: String,
    val nextNodeId: String
)