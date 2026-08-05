package com.picke.presentation.ui.my.notice.model

data class NoticeEventItem(
    val id: String,
    val type: String,
    val title: String,
    val date: String,
    val content: String,
    val isRead: Boolean
)
