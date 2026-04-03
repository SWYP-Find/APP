package com.picke.app.domain.model

data class AlarmItemBoard(
    val notificationId: Long,
    val category: String, // "ALL", "CONTENT", "NOTICE", "EVENT"
    val detailCode: String,
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