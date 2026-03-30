package com.swyp4.team2.data.model

import com.swyp4.team2.domain.model.AlarmItemBoard
import com.swyp4.team2.domain.model.AlarmPageBoard

data class AlarmItemDto(
    val notificationId: Long?,
    val category: String?,
    val detailCode: Int?,
    val title: String?,
    val body: String?,
    val referenceId: Long?,
    val isRead: Boolean?,
    val createdAt: String?
)

data class AlarmPageDto(
    val items: List<AlarmItemDto>?,
    val hasNext: Boolean?
)

fun AlarmItemDto.toDomainModel() = AlarmItemBoard(
    notificationId = this.notificationId ?: 0L,
    category = this.category ?: "ALL",
    detailCode = this.detailCode ?: 0,
    title = this.title ?: "",
    body = this.body ?: "",
    referenceId = this.referenceId ?: 0L,
    isRead = this.isRead ?: false,
    createdAt = this.createdAt ?: ""
)

fun AlarmPageDto.toDomainModel() = AlarmPageBoard(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    hasNext = this.hasNext ?: false
)