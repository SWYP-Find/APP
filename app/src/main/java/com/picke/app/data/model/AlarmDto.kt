package com.picke.app.data.model

import com.picke.app.domain.model.AlarmItemBoard
import com.picke.app.domain.model.AlarmPageBoard

data class AlarmItemDto(
    val notificationId: Long?,
    val category: String?,
    val detailCode: String?,
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

data class AlarmDetailDto(
    val notificationId: Long?,
    val category: String?,
    val detailCode: String?,
    val title: String?,
    val body: String?,
    val referenceId: Long?,
    val isRead: Boolean?,
    val createdAt: String?,
    val readAt: String?
)

data class AlarmDetailBoard(
    val notificationId: Long,
    val category: String,
    val detailCode: String,
    val title: String,
    val body: String,
    val referenceId: Long,
    val isRead: Boolean,
    val createdAt: String,
    val readAt: String
)

// DTO -> Domain Mapper
fun AlarmDetailDto.toDomainModel() = AlarmDetailBoard(
    notificationId = this.notificationId ?: 0L,
    category = this.category ?: "ALL",
    detailCode = this.detailCode ?: "",
    title = this.title ?: "",
    body = this.body ?: "",
    referenceId = this.referenceId ?: 0L,
    isRead = this.isRead ?: false,
    createdAt = this.createdAt?.take(10) ?: "",
    readAt = this.readAt?.take(10) ?: ""
)

fun AlarmItemDto.toDomainModel() = AlarmItemBoard(
    notificationId = this.notificationId ?: 0L,
    category = this.category ?: "ALL",
    detailCode = this.detailCode ?: "",
    title = this.title ?: "",
    body = this.body ?: "",
    referenceId = this.referenceId ?: 0L,
    isRead = this.isRead ?: false,
    createdAt = this.createdAt?.take(10) ?: ""
)

fun AlarmPageDto.toDomainModel() = AlarmPageBoard(
    items = this.items?.map { it.toDomainModel() } ?: emptyList(),
    hasNext = this.hasNext ?: false
)