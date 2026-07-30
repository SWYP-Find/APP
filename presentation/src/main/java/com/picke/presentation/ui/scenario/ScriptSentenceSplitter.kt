package com.picke.presentation.ui.scenario

import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel

// 노드 하나의 스크립트들을 문장 단위로 쪼개고, 각 문장의 시작 시각을 글자 수 비례로 배분한다.
fun splitScriptsBySentence(
    originalScripts: List<ScenarioScriptUiModel>,
    totalNodeDurationMs: Long
): List<ScenarioScriptUiModel> {
    val result = mutableListOf<ScenarioScriptUiModel>()

    for (i in originalScripts.indices) {
        val current = originalScripts[i]
        val nextTimeMs = if (i < originalScripts.lastIndex) originalScripts[i + 1].startTimeMs else totalNodeDurationMs

        val sentences = current.displayText
            .split(Regex("(?<=[.!?])\\s+"))
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        if (sentences.isEmpty()) continue

        val totalChars = sentences.sumOf { it.length }
        val blockDuration = maxOf(0L, nextTimeMs - current.startTimeMs)
        var accumulatedTime = current.startTimeMs

        sentences.forEach { sentence ->
            result.add(current.copy(startTimeMs = accumulatedTime, displayText = sentence))
            val durationForThisSentence = if (totalChars > 0) (blockDuration * sentence.length) / totalChars else 0L
            accumulatedTime += durationForThisSentence
        }
    }
    return result
}
