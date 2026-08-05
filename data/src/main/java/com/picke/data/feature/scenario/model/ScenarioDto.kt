package com.picke.data.feature.scenario.model

import com.picke.domain.feature.scenario.model.ScenarioBoard
import com.picke.domain.feature.scenario.model.ScenarioNode
import com.picke.domain.feature.scenario.model.ScenarioOption
import com.picke.domain.feature.scenario.model.ScenarioPhilosopher
import com.picke.domain.feature.scenario.model.ScenarioScript
import com.picke.domain.feature.scenario.model.SpeakerType

data class ScenarioPhilosopherDto(
    val name: String?,
    val stance: String?,
    val quote: String?,
    val imageUrl: String?
)

data class ScenarioResponseDto(
    val battleId: Long,
    val title: String?,
    val philosophers: List<ScenarioPhilosopherDto>?,
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
    val autoNextNodeId: Long?,
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

fun ScenarioResponseDto.toDomainModel(): ScenarioBoard {
    return ScenarioBoard(
        battleId = battleId.toString(),
        title = title ?: "",
        philosophers = philosophers?.map { dto ->
            ScenarioPhilosopher(
                name = dto.name ?: "",
                stance = dto.stance ?: "",
                quote = dto.quote ?: "",
                imageUrl = dto.imageUrl ?: ""
            )
        } ?: emptyList(),
        isInteractive = isInteractive,
        startNodeId = startNodeId.toString(),
        recommendedPathKey = recommendedPathKey,
        audios = audios,
        nodes = nodes.map { node ->
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
                        speakerType = runCatching { SpeakerType.valueOf(script.speakerType) }
                            .getOrDefault(SpeakerType.UNKNOWN),
                        speakerName = script.speakerName,
                        text = script.text
                    )
                }
            )
        }
    )
}