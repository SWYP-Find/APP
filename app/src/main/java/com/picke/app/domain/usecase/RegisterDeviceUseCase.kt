package com.picke.app.domain.usecase

import com.picke.app.domain.repository.DeviceRepository
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(
    private val deviceRepository: DeviceRepository
) {
    suspend operator fun invoke(fcmToken: String): Result<Unit> {
        return deviceRepository.registerDevice(fcmToken)
    }
}
