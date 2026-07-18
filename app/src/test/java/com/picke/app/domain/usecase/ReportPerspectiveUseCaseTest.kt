package com.picke.app.domain.usecase

import com.picke.app.domain.repository.PerspectiveRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReportPerspectiveUseCaseTest {

    private lateinit var perspectiveRepository: PerspectiveRepository
    private lateinit var useCase: ReportPerspectiveUseCase

    @Before
    fun setUp() {
        perspectiveRepository = mockk()
        useCase = ReportPerspectiveUseCase(perspectiveRepository)
    }

    @Test
    fun `신고 요청이 성공하면 Reported를 반환한다`() = runTest {
        coEvery { perspectiveRepository.reportPerspective(1L) } returns Result.success("OK")

        val result = useCase(1L)

        assertEquals(ReportPerspectiveResult.Reported, result.getOrNull())
    }

    @Test
    fun `이미 신고한 경우 AlreadyReported로 변환한다`() = runTest {
        coEvery { perspectiveRepository.reportPerspective(1L) } returns
            Result.failure(RuntimeException("ALREADY_REPORTED"))

        val result = useCase(1L)

        assertEquals(ReportPerspectiveResult.AlreadyReported, result.getOrNull())
    }

    @Test
    fun `그 외 에러는 실패로 그대로 전파한다`() = runTest {
        val error = RuntimeException("network error")
        coEvery { perspectiveRepository.reportPerspective(1L) } returns Result.failure(error)

        val result = useCase(1L)

        assertTrue(result.isFailure)
        assertEquals("network error", result.exceptionOrNull()?.message)
    }
}
