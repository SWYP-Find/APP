package com.picke.domain.feature.device.repository

interface DeviceRepository {
    /**
     * 디바이스 푸시 토큰 등록 API (로그인 성공 직후 / 토큰 갱신 시)
     *
     * @param fcmToken FCM에서 발급받은 디바이스 토큰
     * @return 등록 성공 시 [Result.success] 반환
     */
    suspend fun registerDevice(fcmToken: String): Result<Unit>

    /**
     * 디바이스 푸시 토큰 해제 API (로그아웃 시)
     *
     * @param fcmToken 해제할 FCM 토큰
     * @return 해제 성공 시 [Result.success] 반환 (미등록 토큰도 200 반환)
     */
    suspend fun unregisterDevice(fcmToken: String): Result<Unit>
}