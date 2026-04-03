package com.picke.app.domain.model

data class NoticeEventItem(
    val id: String,
    val type: String,
    val title: String,
    val date: String,
    val content: String,
    val isRead: Boolean
)
