package com.picke.domain.usecase.alarm

import com.picke.domain.repository.AlarmRepository

class GetUnreadAlarmStatusUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(category: String? = null): Result<Boolean> {
        return alarmRepository.hasUnreadAlarms(category)
    }
}
