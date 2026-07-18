package com.picke.app.domain.usecase

import com.picke.app.domain.model.AlarmDetailBoard
import com.picke.app.domain.repository.AlarmRepository
import javax.inject.Inject

class GetAlarmDetailUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(notificationId: Long): Result<AlarmDetailBoard> {
        return alarmRepository.getAlarmDetail(notificationId)
    }
}
