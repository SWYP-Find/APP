package com.picke.domain.repository

import com.picke.domain.model.AlarmDetailBoard
import com.picke.domain.model.AlarmPageBoard

interface AlarmRepository {
    suspend fun getAlarms(category: String, page: Int, size: Int): Result<AlarmPageBoard>
    suspend fun hasUnreadAlarms(category: String? = null): Result<Boolean>
    suspend fun getAlarmDetail(notificationId: Long): Result<AlarmDetailBoard>
    suspend fun readAlarm(notificationId: Long): Result<String>
    suspend fun readAllAlarms(): Result<Unit>
}