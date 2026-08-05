package com.picke.domain.feature.alarm.usecase

import com.picke.domain.feature.alarm.model.AlarmPageBoard
import com.picke.domain.feature.alarm.repository.AlarmRepository

class GetAlarmsUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(category: String, page: Int, size: Int): Result<AlarmPageBoard> {
        return alarmRepository.getAlarms(category, page, size)
    }
}
