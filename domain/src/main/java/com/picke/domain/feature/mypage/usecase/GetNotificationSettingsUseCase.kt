package com.picke.domain.feature.mypage.usecase

import com.picke.domain.feature.mypage.model.NotificationSettingsBoard
import com.picke.domain.feature.mypage.repository.MyPageRepository

class GetNotificationSettingsUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<NotificationSettingsBoard> {
        return myPageRepository.getNotificationSettings()
    }
}
