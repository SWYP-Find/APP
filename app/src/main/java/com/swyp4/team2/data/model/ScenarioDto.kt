package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.ScenarioBoard
import com.swyp4.team2.domain.model.ScenarioNode
import com.swyp4.team2.domain.model.ScenarioOption
import com.swyp4.team2.domain.model.ScenarioScript
import com.swyp4.team2.domain.model.SpeakerType

data class ScenarioResponseDto(
    val battleId: String,
    val isInteractive: Boolean,
    val startNodeId: String,
    val recommendedPathKey: String,
    val audios: Map<String, String>,
    val nodes: List<ScenarioNodeDto>
)

data class ScenarioNodeDto(
    val nodeId: String,
    val nodeName: String,
    val audioDuration: Int,
    val autoNextNodeId: String?,
    val scripts: List<ScenarioScriptDto>,
    val interactiveOptions: List<ScenarioOptionDto>
)

data class ScenarioScriptDto(
    val scriptId: String,
    val startTimeMs: Long,
    val speakerType: String,
    val speakerName: String,
    val text: String
)

data class ScenarioOptionDto(
    val label: String,
    val nextNodeId: String
)

// Dto -> Domain
fun ScenarioResponseDto.toDomainModel(): ScenarioBoard {
    return ScenarioBoard(
        battleId = this.battleId,
        isInteractive = this.isInteractive,
        startNodeId = this.startNodeId,
        recommendedPathKey = this.recommendedPathKey,
        audios = this.audios,
        nodes = this.nodes.map { node ->
            ScenarioNode(
                nodeId = node.nodeId,
                nodeName = node.nodeName,
                audioDuration = node.audioDuration,
                autoNextNodeId = node.autoNextNodeId,
                interactiveOptions = node.interactiveOptions.map {
                    ScenarioOption(
                        it.label,
                        it.nextNodeId
                    )
                },
                scripts = node.scripts.map { script ->
                    ScenarioScript(
                        scriptId = script.scriptId,
                        startTimeMs = script.startTimeMs,
                        speakerType = runCatching { SpeakerType.valueOf(script.speakerType) }.getOrDefault(
                            SpeakerType.UNKNOWN
                        ),
                        speakerName = script.speakerName,
                        text = script.text
                    )
                }
            )
        }
    )
}