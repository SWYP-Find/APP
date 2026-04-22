package com.picke.app.ui.scenario.model

import com.picke.app.domain.model.ScenarioBoard
import com.picke.app.domain.model.SpeakerType

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

// Domain -> UI Mapper
fun ScenarioBoard.toUiModel(): ScenarioUiModel {
    val philosopherImageMap = this.philosophers.associate { it.label to it.imageUrl }

    val nodeMap = this.nodes.associateBy(
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
        battleId = this.battleId,
        isInteractive = this.isInteractive,
        startNodeId = this.startNodeId,
        audios = this.audios,
        nodes = nodeMap
    )
}