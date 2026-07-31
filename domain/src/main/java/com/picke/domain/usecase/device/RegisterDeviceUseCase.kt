package com.picke.domain.usecase.device

import com.picke.domain.repository.DeviceRepository

class RegisterDeviceUseCase(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(fcmToken: String): Result<Unit> {
        return deviceRepository.registerDevice(fcmToken)
    }
}
