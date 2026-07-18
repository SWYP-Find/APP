package com.picke.app.domain.usecase.alarm

import com.picke.app.domain.repository.AlarmRepository
import javax.inject.Inject

class ReadAllAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(): Result<String> {
        return alarmRepository.readAllAlarms()
    }
}
