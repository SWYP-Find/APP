package com.swyp4.team2.domain.model

data class AlarmItemBoard(
    val notificationId: Long,
    val category: String, // "ALL", "CONTENT", "NOTICE", "EVENT"
    val detailCode: Int,
    val title: String,
    val body: String,
    val referenceId: Long,
    val isRead: Boolean,
    val createdAt: String
)

data class AlarmPageBoard(
    val items: List<AlarmItemBoard>,
    val hasNext: Boolean
)