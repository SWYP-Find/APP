package com.picke.app.domain.usecase

import com.picke.app.domain.model.PerspectiveStatusBoard
import com.picke.app.domain.model.PerspectiveUpdateBoard
import com.picke.app.domain.repository.PerspectiveRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubmitPerspectiveUseCaseTest {

    private lateinit var perspectiveRepository: PerspectiveRepository
    private lateinit var useCase: SubmitPerspectiveUseCase

    @Before
    fun setUp() {
        perspectiveRepository = mockk()
        useCase = SubmitPerspectiveUseCase(perspectiveRepository)
    }

    @Test
    fun `editingPerspectiveId가 없으면 신규 작성 요청을 보낸다`() = runTest {
        coEvery { perspectiveRepository.createPerspective(1L, "content") } returns
            Result.success(PerspectiveStatusBoard(perspectiveId = 10L, status = "PUBLISHED", createdAt = "2026-07-18"))

        val result = useCase(battleId = 1L, editingPerspectiveId = null, content = "content")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { perspectiveRepository.createPerspective(1L, "content") }
        coVerify(exactly = 0) { perspectiveRepository.updatePerspective(any(), any()) }
    }

    @Test
    fun `editingPerspectiveId가 있으면 수정 요청을 보낸다`() = runTest {
        coEvery { perspectiveRepository.updatePerspective(99L, "edited") } returns
            Result.success(PerspectiveUpdateBoard(perspectiveId = 99L, content = "edited", updatedAt = "2026-07-18"))

        val result = useCase(battleId = 1L, editingPerspectiveId = 99L, content = "edited")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { perspectiveRepository.updatePerspective(99L, "edited") }
        coVerify(exactly = 0) { perspectiveRepository.createPerspective(any(), any()) }
    }

    @Test
    fun `저장소 요청이 실패하면 실패를 그대로 전파한다`() = runTest {
        val error = RuntimeException("network error")
        coEvery { perspectiveRepository.createPerspective(1L, "content") } returns Result.failure(error)

        val result = useCase(battleId = 1L, editingPerspectiveId = null, content = "content")

        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() === error)
    }
}
