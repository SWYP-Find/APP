package com.picke.domain.feature.auth.usecase

import com.picke.domain.feature.auth.repository.AuthRepository
import com.picke.domain.feature.device.repository.DeviceRepository

class LogoutUseCase(
    private val authRepository: AuthRepository,
    private val deviceRepository: DeviceRepository
) {
    // FCM 토큰 해제는 실패하더라도 로그아웃 자체는 계속 진행한다 (미등록 토큰 등으로 실패할 수 있음).
    suspend operator fun invoke(fcmToken: String?): Result<Unit> {
        fcmToken?.let { deviceRepository.unregisterDevice(it) }
        return authRepository.logout()
    }
}
