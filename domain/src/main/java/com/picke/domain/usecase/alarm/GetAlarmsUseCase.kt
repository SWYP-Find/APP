package com.picke.domain.usecase.alarm

import com.picke.domain.model.AlarmPageBoard
import com.picke.domain.repository.AlarmRepository

class GetAlarmsUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(category: String, page: Int, size: Int): Result<AlarmPageBoard> {
        return alarmRepository.getAlarms(category, page, size)
    }
}
