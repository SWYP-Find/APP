package com.picke.app.domain.repository

interface DeviceRepository {
    /**
     * 디바이스 푸시 토큰 등록 API
     *
     * @param fcmToken FCM에서 발급받은 디바이스 토큰
     * @return 등록 성공 시 [Result.success] 반환
     */
    suspend fun registerDevice(fcmToken: String): Result<Unit>
}
