package com.picke.app.domain.usecase.alarm

import com.picke.app.domain.repository.AlarmRepository
import javax.inject.Inject

class ReadAlarmUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(notificationId: Long): Result<String> {
        return alarmRepository.readAlarm(notificationId)
    }
}
