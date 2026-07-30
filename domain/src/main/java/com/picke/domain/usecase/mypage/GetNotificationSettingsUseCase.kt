package com.picke.domain.usecase.mypage

import com.picke.domain.model.NotificationSettingsBoard
import com.picke.domain.repository.MyPageRepository

class GetNotificationSettingsUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<NotificationSettingsBoard> {
        return myPageRepository.getNotificationSettings()
    }
}
