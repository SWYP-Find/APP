package com.picke.domain.usecase.mypage

import com.picke.domain.model.NotificationSettingsBoard
import com.picke.domain.repository.MyPageRepository

class UpdateNotificationSettingsUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(settings: NotificationSettingsBoard): Result<NotificationSettingsBoard> {
        return myPageRepository.updateNotificationSettings(settings)
    }
}
