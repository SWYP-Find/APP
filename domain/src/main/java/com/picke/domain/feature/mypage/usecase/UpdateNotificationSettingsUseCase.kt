package com.picke.domain.feature.mypage.usecase

import com.picke.domain.feature.mypage.model.NotificationSettingsBoard
import com.picke.domain.feature.mypage.repository.MyPageRepository

class UpdateNotificationSettingsUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(settings: NotificationSettingsBoard): Result<NotificationSettingsBoard> {
        return myPageRepository.updateNotificationSettings(settings)
    }
}
