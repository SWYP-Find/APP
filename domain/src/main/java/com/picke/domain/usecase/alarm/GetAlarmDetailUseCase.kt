package com.picke.domain.usecase.alarm

import com.picke.domain.model.AlarmDetailBoard
import com.picke.domain.repository.AlarmRepository

class GetAlarmDetailUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(notificationId: Long): Result<AlarmDetailBoard> {
        return alarmRepository.getAlarmDetail(notificationId)
    }
}
