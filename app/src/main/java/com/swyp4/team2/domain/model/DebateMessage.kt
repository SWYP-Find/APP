package com.swyp4.team2.domain.model

data class DebateMessage(
    val id:Int,
    val startTimeMs: Long,
    val speakerType: SpeakerType,
    val speakerName: String,
    val text: String,
    val isPlaying: Boolean = true,
)

enum class SpeakerType{
    A, B
}