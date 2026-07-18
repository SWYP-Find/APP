package com.picke.app.domain.usecase

import com.picke.app.domain.repository.AuthRepository
import com.picke.app.domain.repository.DeviceRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class LogoutUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var deviceRepository: DeviceRepository
    private lateinit var useCase: LogoutUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        deviceRepository = mockk()
        useCase = LogoutUseCase(authRepository, deviceRepository)
    }

    @Test
    fun `fcmToken이 있으면 디바이스 해제 후 로그아웃한다`() = runTest {
        coEvery { deviceRepository.unregisterDevice("token") } returns Result.success(Unit)
        coEvery { authRepository.logout() } returns Result.success(Unit)

        val result = useCase("token")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { deviceRepository.unregisterDevice("token") }
        coVerify(exactly = 1) { authRepository.logout() }
    }

    @Test
    fun `fcmToken이 없으면 디바이스 해제 없이 로그아웃한다`() = runTest {
        coEvery { authRepository.logout() } returns Result.success(Unit)

        val result = useCase(null)

        assertTrue(result.isSuccess)
        coVerify(exactly = 0) { deviceRepository.unregisterDevice(any()) }
        coVerify(exactly = 1) { authRepository.logout() }
    }

    @Test
    fun `디바이스 해제가 실패해도 로그아웃은 계속 진행한다`() = runTest {
        coEvery { deviceRepository.unregisterDevice("token") } returns
            Result.failure(RuntimeException("device not found"))
        coEvery { authRepository.logout() } returns Result.success(Unit)

        val result = useCase("token")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { authRepository.logout() }
    }

    @Test
    fun `로그아웃 자체가 실패하면 실패를 반환한다`() = runTest {
        coEvery { deviceRepository.unregisterDevice("token") } returns Result.success(Unit)
        val error = RuntimeException("network error")
        coEvery { authRepository.logout() } returns Result.failure(error)

        val result = useCase("token")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() === error)
    }
}
