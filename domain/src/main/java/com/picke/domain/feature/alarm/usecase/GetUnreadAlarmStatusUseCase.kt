package com.picke.domain.feature.alarm.usecase

import com.picke.domain.feature.alarm.repository.AlarmRepository

class GetUnreadAlarmStatusUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(category: String? = null): Result<Boolean> {
        return alarmRepository.hasUnreadAlarms(category)
    }
}
