package com.picke.app.domain.usecase

import com.picke.app.domain.model.CommentLikeToggleBoard
import com.picke.app.domain.repository.CommentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ToggleCommentLikeUseCaseTest {

    private lateinit var commentRepository: CommentRepository
    private lateinit var useCase: ToggleCommentLikeUseCase

    @Before
    fun setUp() {
        commentRepository = mockk()
        useCase = ToggleCommentLikeUseCase(commentRepository)
    }

    @Test
    fun `현재 좋아요 상태가 아니면 좋아요를 등록한다`() = runTest {
        val expected = CommentLikeToggleBoard(perspectiveId = 1L, likeCount = 3, isLiked = true)
        coEvery { commentRepository.likeComment(1L) } returns Result.success(expected)

        val result = useCase(commentId = 1L, isCurrentlyLiked = false)

        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { commentRepository.likeComment(1L) }
        coVerify(exactly = 0) { commentRepository.unlikeComment(any()) }
    }

    @Test
    fun `현재 좋아요 상태이면 좋아요를 취소한다`() = runTest {
        val expected = CommentLikeToggleBoard(perspectiveId = 1L, likeCount = 2, isLiked = false)
        coEvery { commentRepository.unlikeComment(1L) } returns Result.success(expected)

        val result = useCase(commentId = 1L, isCurrentlyLiked = true)

        assertEquals(expected, result.getOrNull())
        coVerify(exactly = 1) { commentRepository.unlikeComment(1L) }
        coVerify(exactly = 0) { commentRepository.likeComment(any()) }
    }
}
