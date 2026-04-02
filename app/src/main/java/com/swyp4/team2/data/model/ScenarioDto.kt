package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.ScenarioBoard
import com.swyp4.team2.domain.model.ScenarioNode
import com.swyp4.team2.domain.model.ScenarioOption
import com.swyp4.team2.domain.model.ScenarioScript
import com.swyp4.team2.domain.model.SpeakerType

data class ScenarioPhilosopherDto(
    val label: String?,
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

// 2. Dto -> Domain 매퍼
fun ScenarioResponseDto.toDomainModel(): ScenarioBoard {
    return ScenarioBoard(
        battleId = this.battleId?.toString() ?: "",
        title = this.title ?: "",

        philosophers = this.philosophers?.map { philosopherDto ->
            com.swyp4.team2.domain.model.ScenarioPhilosopher(
                label = philosopherDto.label ?: "",
                name = philosopherDto.name ?: "",
                stance = philosopherDto.stance ?: "",
                quote = philosopherDto.quote ?: "",
                imageUrl = philosopherDto.imageUrl ?: ""
            )
        } ?: emptyList(),

        isInteractive = this.isInteractive ?: false,
        startNodeId = this.startNodeId?.toString() ?: "",
        recommendedPathKey = this.recommendedPathKey ?: "COMMON",
        audios = this.audios ?: emptyMap(),
        nodes = this.nodes?.map { node ->
            com.swyp4.team2.domain.model.ScenarioNode(
                nodeId = node.nodeId?.toString() ?: "",
                nodeName = node.nodeName ?: "",
                audioDuration = node.audioDuration ?: 0,
                autoNextNodeId = node.autoNextNodeId?.toString(),
                interactiveOptions = node.interactiveOptions?.map {
                    com.swyp4.team2.domain.model.ScenarioOption(
                        label = it.label ?: "",
                        nextNodeId = it.nextNodeId?.toString() ?: ""
                    )
                } ?: emptyList(),
                scripts = node.scripts?.map { script ->
                    com.swyp4.team2.domain.model.ScenarioScript(
                        scriptId = script.scriptId?.toString() ?: "",
                        startTimeMs = script.startTimeMs ?: 0L,
                        speakerType = runCatching { com.swyp4.team2.domain.model.SpeakerType.valueOf(script.speakerType ?: "UNKNOWN") }.getOrDefault(com.swyp4.team2.domain.model.SpeakerType.UNKNOWN),
                        speakerName = script.speakerName ?: "",
                        text = script.text ?: ""
                    )
                } ?: emptyList()
            )
        } ?: emptyList()
    )
}