package com.swyp4.team2.data.model

import com.google.gson.annotations.SerializedName
import com.swyp4.team2.domain.model.ScenarioBoard
import com.swyp4.team2.domain.model.ScenarioNode
import com.swyp4.team2.domain.model.ScenarioOption
import com.swyp4.team2.domain.model.ScenarioScript
import com.swyp4.team2.domain.model.SpeakerType

data class ScenarioDto(
    @SerializedName("battleId") val battleId: String,
    @SerializedName("isInteractive") val isInteractive: Boolean,
    @SerializedName("startNodeId") val startNodeId: String,
    @SerializedName("recommendedPathKey") val recommendedPathKey: String,
    @SerializedName("audios") val audios: Map<String, String>,
    @SerializedName("nodes") val nodes: List<ScenarioNodeDto>
)

data class ScenarioNodeDto(
    @SerializedName("nodeId") val nodeId: String,
    @SerializedName("nodeName") val nodeName: String,
    @SerializedName("audioDuration") val audioDuration: Int,
    @SerializedName("autoNextNodeId") val autoNextNodeId: String?,
    @SerializedName("scripts") val scripts: List<ScenarioScriptDto>,
    @SerializedName("interactiveOptions") val interactiveOptions: List<ScenarioOptionDto>
)

data class ScenarioScriptDto(
    @SerializedName("scriptId") val scriptId: String,
    @SerializedName("startTimeMs") val startTimeMs: Long,
    @SerializedName("speakerType") val speakerType: String,
    @SerializedName("speakerName") val speakerName: String,
    @SerializedName("text") val text: String
)

data class ScenarioOptionDto(
    @SerializedName("label") val label: String,
    @SerializedName("nextNodeId") val nextNodeId: String
)

// Dto -> Domain
fun ScenarioDto.toDomainModel(): ScenarioBoard {
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