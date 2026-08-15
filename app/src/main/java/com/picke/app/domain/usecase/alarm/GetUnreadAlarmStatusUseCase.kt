package com.picke.app.domain.usecase.alarm

import com.picke.app.domain.repository.AlarmRepository
import javax.inject.Inject

class GetUnreadAlarmStatusUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke(category: String? = null): Result<Boolean> {
        return alarmRepository.hasUnreadAlarms(category)
    }
}
