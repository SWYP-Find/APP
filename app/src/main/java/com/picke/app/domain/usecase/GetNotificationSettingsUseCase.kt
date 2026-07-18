package com.picke.app.domain.usecase

import com.picke.app.domain.model.NotificationSettingsBoard
import com.picke.app.domain.repository.MyPageRepository
import javax.inject.Inject

class GetNotificationSettingsUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<NotificationSettingsBoard> {
        return myPageRepository.getNotificationSettings()
    }
}
