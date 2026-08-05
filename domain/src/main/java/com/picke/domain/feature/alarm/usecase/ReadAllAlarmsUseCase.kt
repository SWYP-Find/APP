package com.picke.domain.feature.alarm.usecase

import com.picke.domain.feature.alarm.repository.AlarmRepository

class ReadAllAlarmsUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return alarmRepository.readAllAlarms()
    }
}
