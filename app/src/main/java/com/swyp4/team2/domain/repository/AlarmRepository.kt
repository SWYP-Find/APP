package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.AlarmPageBoard

interface AlarmRepository {
    suspend fun getAlarms(category: String, page: Int, size: Int): Result<AlarmPageBoard>
    suspend fun readAlarm(notificationId: Long): Result<String>
    suspend fun readAllAlarms(): Result<String>
}