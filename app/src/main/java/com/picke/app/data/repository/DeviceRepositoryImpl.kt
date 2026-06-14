package com.picke.app.data.repository

import com.picke.app.data.model.RegisterDeviceRequest
import com.picke.app.data.remote.DeviceApi
import com.picke.app.domain.repository.DeviceRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceRepositoryImpl @Inject constructor(
    private val deviceApi: DeviceApi
) : DeviceRepository {

    override suspend fun registerDevice(fcmToken: String): Result<Unit> {
        return try {
            val response = deviceApi.registerDevice(
                RegisterDeviceRequest(fcmToken = fcmToken, platform = "ANDROID")
            )
            if (response.statusCode == 200) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.error?.message ?: "디바이스 등록 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun unregisterDevice(fcmToken: String): Result<Unit> {
        return try {
            val response = deviceApi.deleteDevice(fcmToken)
            if (response.statusCode == 200) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.error?.message ?: "디바이스 해제 실패"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
