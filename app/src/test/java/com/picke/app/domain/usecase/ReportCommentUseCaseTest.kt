package com.picke.app.domain.usecase

import com.picke.app.domain.repository.CommentRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ReportCommentUseCaseTest {

    private lateinit var commentRepository: CommentRepository
    private lateinit var useCase: ReportCommentUseCase

    @Before
    fun setUp() {
        commentRepository = mockk()
        useCase = ReportCommentUseCase(commentRepository)
    }

    @Test
    fun `신고 요청이 성공하면 Reported를 반환한다`() = runTest {
        coEvery { commentRepository.reportComment(1L, 2L) } returns Result.success("OK")

        val result = useCase(perspectiveId = 1L, commentId = 2L)

        assertEquals(ReportCommentResult.Reported, result.getOrNull())
    }

    @Test
    fun `이미 신고한 경우 AlreadyReported로 변환한다`() = runTest {
        coEvery { commentRepository.reportComment(1L, 2L) } returns
            Result.failure(RuntimeException("ALREADY_REPORTED"))

        val result = useCase(perspectiveId = 1L, commentId = 2L)

        assertEquals(ReportCommentResult.AlreadyReported, result.getOrNull())
    }

    @Test
    fun `그 외 에러는 실패로 그대로 전파한다`() = runTest {
        val error = RuntimeException("network error")
        coEvery { commentRepository.reportComment(1L, 2L) } returns Result.failure(error)

        val result = useCase(perspectiveId = 1L, commentId = 2L)

        assertTrue(result.isFailure)
        assertEquals("network error", result.exceptionOrNull()?.message)
    }
}
