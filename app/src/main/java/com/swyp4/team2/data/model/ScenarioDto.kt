package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.ScenarioBoard
import com.swyp4.team2.domain.model.ScenarioNode
import com.swyp4.team2.domain.model.ScenarioOption
import com.swyp4.team2.domain.model.ScenarioScript
import com.swyp4.team2.domain.model.SpeakerType

data class ScenarioResponseDto(
    val battleId: Long,
    val isInteractive: Boolean,
    val startNodeId: Long,
    val recommendedPathKey: String,
    val audios: Map<String, String>,
    val nodes: List<ScenarioNodeDto>
)

data class ScenarioNodeDto(
    val nodeId: Long,
    val nodeName: String,
    val audioDuration: Int,
    val autoNextNodeId: Long?, // 이 값은 null이 올 수 있으므로 Long? 로 유지
    val scripts: List<ScenarioScriptDto>,
    val interactiveOptions: List<ScenarioOptionDto>
)

data class ScenarioScriptDto(
    val scriptId: Long,
    val startTimeMs: Long,
    val speakerType: String,
    val speakerName: String,
    val text: String
)

data class ScenarioOptionDto(
    val label: String,
    val nextNodeId: Long
)

// 🌟 2. Dto -> Domain 매퍼: Long으로 받은 ID를 Domain에 맞게 String으로 변환(.toString())
fun ScenarioResponseDto.toDomainModel(): ScenarioBoard {
    return ScenarioBoard(
        battleId = this.battleId.toString(),
        isInteractive = this.isInteractive,
        startNodeId = this.startNodeId.toString(),
        recommendedPathKey = this.recommendedPathKey,
        audios = this.audios ?: emptyMap(), // 혹시 모를 null 방어
        nodes = this.nodes.map { node ->
            ScenarioNode(
                nodeId = node.nodeId.toString(),
                nodeName = node.nodeName,
                audioDuration = node.audioDuration,
                autoNextNodeId = node.autoNextNodeId?.toString(),
                interactiveOptions = node.interactiveOptions.map {
                    ScenarioOption(
                        label = it.label,
                        nextNodeId = it.nextNodeId.toString()
                    )
                },
                scripts = node.scripts.map { script ->
                    ScenarioScript(
                        scriptId = script.scriptId.toString(),
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