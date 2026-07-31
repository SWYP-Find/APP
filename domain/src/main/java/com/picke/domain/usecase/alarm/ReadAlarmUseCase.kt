package com.picke.domain.usecase.alarm

import com.picke.domain.repository.AlarmRepository

class ReadAlarmUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(notificationId: Long): Result<String> {
        return alarmRepository.readAlarm(notificationId)
    }
}
