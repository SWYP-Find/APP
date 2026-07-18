package com.picke.app.ui.scenario

import com.picke.app.domain.model.SpeakerType
import com.picke.app.ui.scenario.model.ScenarioScriptUiModel
import org.junit.Assert.assertEquals
import org.junit.Test

class ScriptSentenceSplitterTest {

    private fun script(startTimeMs: Long, text: String) = ScenarioScriptUiModel(
        scriptId = "s",
        startTimeMs = startTimeMs,
        speakerType = SpeakerType.A,
        speakerName = "철학자",
        displayText = text
    )

    @Test
    fun `문장 여러 개를 글자 수 비례로 시간 배분한다`() {
        // "AA."(3자) + "BBBBBB."(7자), 블록 구간 1000ms -> 300ms / 700ms로 배분
        val scripts = listOf(script(0, "AA. BBBBBB."))

        val result = splitScriptsBySentence(scripts, totalNodeDurationMs = 1000L)

        assertEquals(2, result.size)
        assertEquals("AA.", result[0].displayText)
        assertEquals(0L, result[0].startTimeMs)
        assertEquals("BBBBBB.", result[1].displayText)
        assertEquals(300L, result[1].startTimeMs)
    }

    @Test
    fun `다음 스크립트의 시작시각을 블록 경계로 사용한다`() {
        val scripts = listOf(
            script(0, "첫 문장."),
            script(500, "둘째 문장.")
        )

        val result = splitScriptsBySentence(scripts, totalNodeDurationMs = 10_000L)

        assertEquals(0L, result[0].startTimeMs)
        assertEquals(500L, result[1].startTimeMs)
    }

    @Test
    fun `마지막 스크립트는 totalNodeDurationMs를 블록 경계로 사용한다`() {
        val scripts = listOf(script(800, "마지막 문장."))

        val result = splitScriptsBySentence(scripts, totalNodeDurationMs = 1000L)

        assertEquals(1, result.size)
        assertEquals(800L, result[0].startTimeMs)
    }

    @Test
    fun `빈 문장만 있으면 결과에서 제외한다`() {
        val scripts = listOf(script(0, "   "))

        val result = splitScriptsBySentence(scripts, totalNodeDurationMs = 1000L)

        assertEquals(0, result.size)
    }

    @Test
    fun `문장부호가 없으면 하나의 문장으로 취급한다`() {
        val scripts = listOf(script(0, "문장부호 없는 텍스트"))

        val result = splitScriptsBySentence(scripts, totalNodeDurationMs = 1000L)

        assertEquals(1, result.size)
        assertEquals("문장부호 없는 텍스트", result[0].displayText)
        assertEquals(0L, result[0].startTimeMs)
    }
}
