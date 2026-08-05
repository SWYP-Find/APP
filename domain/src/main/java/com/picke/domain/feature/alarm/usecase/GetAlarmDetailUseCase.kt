package com.picke.domain.feature.alarm.usecase

import com.picke.domain.feature.alarm.model.AlarmDetailBoard
import com.picke.domain.feature.alarm.repository.AlarmRepository

class GetAlarmDetailUseCase(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(notificationId: Long): Result<AlarmDetailBoard> {
        return alarmRepository.getAlarmDetail(notificationId)
    }
}
