package com.picke.app.domain.model

enum class SpeakerType {
    NARRATOR, A, B, USER, UNKNOWN
}

data class ScenarioPhilosopher(
    val label: String,
    val name: String,
    val stance: String,
    val quote: String,
    val imageUrl: String
)

data class ScenarioBoard(
    val battleId: String,
    val title: String,
    val philosophers: List<ScenarioPhilosopher>,
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