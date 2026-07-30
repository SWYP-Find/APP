package com.picke.domain.usecase.alarm

import com.picke.domain.repository.AlarmRepository

class ReadAllAlarmsUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        return alarmRepository.readAllAlarms()
    }
}
