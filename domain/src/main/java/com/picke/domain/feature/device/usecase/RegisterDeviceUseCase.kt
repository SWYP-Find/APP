package com.picke.domain.feature.device.usecase

import com.picke.domain.feature.device.repository.DeviceRepository

class RegisterDeviceUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(fcmToken: String): Result<Unit> {
        return deviceRepository.registerDevice(fcmToken)
    }
}
