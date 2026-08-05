package com.picke.domain.feature.alarm.model

data class AlarmItemBoard(
    val notificationId: Long,
    val perspectiveId: Long,
    val category: String, // "ALL", "CONTENT", "NOTICE", "EVENT"
    val detailCode: String,
    val title: String,
    val body: String,
    val referenceId: Long,
    val isRead: Boolean,
    val createdAt: String
)

data class AlarmDetailBoard(
    val notificationId: Long,
    val perspectiveId: Long,
    val category: String,
    val detailCode: String,
    val title: String,
    val body: String,
    val referenceId: Long,
    val isRead: Boolean,
    val createdAt: String,
    val readAt: String
)

data class AlarmPageBoard(
    val items: List<AlarmItemBoard>,
    val hasNext: Boolean
)