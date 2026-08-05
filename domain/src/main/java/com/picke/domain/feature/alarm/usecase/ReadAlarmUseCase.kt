package com.picke.domain.feature.alarm.usecase

import com.picke.domain.feature.alarm.repository.AlarmRepository

class ReadAlarmUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(notificationId: Long): Result<String> {
        return alarmRepository.readAlarm(notificationId)
    }
}
