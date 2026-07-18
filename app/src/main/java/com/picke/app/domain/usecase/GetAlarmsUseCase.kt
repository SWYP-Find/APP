package com.picke.app.domain.usecase

import com.picke.app.domain.model.AlarmPageBoard
import com.picke.app.domain.repository.AlarmRepository
import javax.inject.Inject

class GetAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(category: String, page: Int, size: Int): Result<AlarmPageBoard> {
        return alarmRepository.getAlarms(category, page, size)
    }
}
