package com.picke.app.domain.usecase

import com.picke.app.domain.model.CommentCreateBoard
import com.picke.app.domain.model.CommentUpdateBoard
import com.picke.app.domain.model.CommentUserBoard
import com.picke.app.domain.repository.CommentRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubmitCommentUseCaseTest {

    private lateinit var commentRepository: CommentRepository
    private lateinit var useCase: SubmitCommentUseCase

    @Before
    fun setUp() {
        commentRepository = mockk()
        useCase = SubmitCommentUseCase(commentRepository)
    }

    @Test
    fun `editingCommentId가 없으면 신규 작성 요청을 보낸다`() = runTest {
        coEvery { commentRepository.createComment(1L, "content") } returns
            Result.success(
                CommentCreateBoard(
                    commentId = 10L,
                    user = CommentUserBoard("tag", "nick", "type", "url"),
                    stance = "A",
                    content = "content",
                    likeCount = 0,
                    isLiked = false,
                    isMine = true,
                    createdAt = "2026-07-18"
                )
            )

        val result = useCase(perspectiveId = 1L, editingCommentId = null, content = "content")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { commentRepository.createComment(1L, "content") }
        coVerify(exactly = 0) { commentRepository.updateComment(any(), any(), any()) }
    }

    @Test
    fun `editingCommentId가 있으면 수정 요청을 보낸다`() = runTest {
        coEvery { commentRepository.updateComment(1L, 99L, "edited") } returns
            Result.success(CommentUpdateBoard(commentId = 99L, content = "edited", updatedAt = "2026-07-18"))

        val result = useCase(perspectiveId = 1L, editingCommentId = 99L, content = "edited")

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { commentRepository.updateComment(1L, 99L, "edited") }
        coVerify(exactly = 0) { commentRepository.createComment(any(), any()) }
    }
}
