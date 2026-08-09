package com.picke.presentation.ui.scenario.model

import com.picke.domain.feature.scenario.model.ScenarioBoard
import com.picke.domain.feature.scenario.model.SpeakerType

data class ScenarioUiModel(
    val battleId: String,
    val isInteractive: Boolean,
    val startNodeId: String,
    val audios: Map<String, String>,
    val nodes: Map<String, ScenarioNodeUiModel>
)

data class ScenarioNodeUiModel(
    val nodeId: String,
    val nodeName: String,
    val audioDuration: Int,
    val autoNextNodeId: String?,
    val scripts: List<ScenarioScriptUiModel>,
    val interactiveOptions: List<ScenarioOptionUiModel>
)

data class ScenarioScriptUiModel(
    val scriptId: String,
    val startTimeMs: Long,
    val speakerType: SpeakerType,
    val speakerName: String,
    val displayText: String,
    val profileImageUrl: String? = null
)

data class ScenarioOptionUiModel(
    val label: String,
    val nextNodeId: String
)

fun ScenarioBoard.toUiModel(): ScenarioUiModel {
    val philosopherImageMap = philosophers.mapIndexedNotNull { index, philosopher ->
        val speakerType = when (index) {
            0 -> SpeakerType.A
            1 -> SpeakerType.B
            else -> null
        } ?: return@mapIndexedNotNull null
        speakerType.name to philosopher.imageUrl
    }.toMap()

    val nodeMap = nodes.associateBy(
        keySelector = { it.nodeId },
        valueTransform = { node ->
            ScenarioNodeUiModel(
                nodeId = node.nodeId,
                nodeName = node.nodeName,
                audioDuration = node.audioDuration,
                autoNextNodeId = node.autoNextNodeId,
                interactiveOptions = node.interactiveOptions.map {
                    ScenarioOptionUiModel(it.label, it.nextNodeId)
                },
                scripts = node.scripts.map { script ->
                    ScenarioScriptUiModel(
                        scriptId = script.scriptId,
                        startTimeMs = script.startTimeMs,
                        speakerType = script.speakerType,
                        speakerName = script.speakerName,
                        displayText = script.text.replace(Regex("<[^>]*>"), ""),
                        profileImageUrl = philosopherImageMap[script.speakerType.name]
                    )
                }
            )
        }
    )

    return ScenarioUiModel(
        battleId = battleId,
        isInteractive = isInteractive,
        startNodeId = startNodeId,
        audios = audios,
        nodes = nodeMap
    )
}