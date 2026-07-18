package com.picke.app.domain.usecase

import com.picke.app.domain.model.PerspectiveLikeToggleBoard
import com.picke.app.domain.repository.PerspectiveRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class TogglePerspectiveLikeUseCaseTest {

    private lateinit var perspectiveRepository: PerspectiveRepository
    private lateinit var useCase: TogglePerspectiveLikeUseCase

    @Before
    fun setUp() {
        perspectiveRepository = mockk()
        useCase = TogglePerspectiveLikeUseCase(perspectiveRepository)
    }

    @Test
    fun `현재 좋아요 상태가 아니면 좋아요를 등록한다`() = runTest {
        val expected = PerspectiveLikeToggleBoard(perspectiveId = 1L, likeCount = 5, isLiked = true)
        coEvery { perspectiveRepository.likePerspective(1L) } returns Result.success(expected)

        val result = useCase(perspectiveId = 1L, isCurrentlyLiked = false)

        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { perspectiveRepository.likePerspective(1L) }
        coVerify(exactly = 0) { perspectiveRepository.unlikePerspective(any()) }
    }

    @Test
    fun `현재 좋아요 상태이면 좋아요를 취소한다`() = runTest {
        val expected = PerspectiveLikeToggleBoard(perspectiveId = 1L, likeCount = 4, isLiked = false)
        coEvery { perspectiveRepository.unlikePerspective(1L) } returns Result.success(expected)

        val result = useCase(perspectiveId = 1L, isCurrentlyLiked = true)

        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { perspectiveRepository.unlikePerspective(1L) }
        coVerify(exactly = 0) { perspectiveRepository.likePerspective(any()) }
    }
}
