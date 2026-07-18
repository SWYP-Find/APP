package com.picke.app.domain.usecase

import com.picke.app.domain.repository.VoteRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SubmitVoteUseCaseTest {

    private lateinit var voteRepository: VoteRepository
    private lateinit var useCase: SubmitVoteUseCase

    @Before
    fun setUp() {
        voteRepository = mockk()
        useCase = SubmitVoteUseCase(voteRepository)
    }

    @Test
    fun `isPreVote가 true이면 사전 투표를 제출한다`() = runTest {
        coEvery { voteRepository.submitPreVote(1L, 2L) } returns Result.success(true)

        val result = useCase(battleId = 1L, optionId = 2L, isPreVote = true)

        assertEquals(SubmitVoteResult.Success, result.getOrNull())
        coVerify(exactly = 1) { voteRepository.submitPreVote(1L, 2L) }
        coVerify(exactly = 0) { voteRepository.submitPostVote(any(), any()) }
    }

    @Test
    fun `isPreVote가 false이면 사후 투표를 제출한다`() = runTest {
        coEvery { voteRepository.submitPostVote(1L, 2L) } returns Result.success(true)

        val result = useCase(battleId = 1L, optionId = 2L, isPreVote = false)

        assertEquals(SubmitVoteResult.Success, result.getOrNull())
        coVerify(exactly = 1) { voteRepository.submitPostVote(1L, 2L) }
        coVerify(exactly = 0) { voteRepository.submitPreVote(any(), any()) }
    }

    @Test
    fun `포인트 부족 에러이면 InsufficientPoints로 변환한다`() = runTest {
        coEvery { voteRepository.submitPreVote(1L, 2L) } returns
            Result.failure(RuntimeException("CREDIT_400_INSUFFICIENT: 포인트가 부족합니다"))

        val result = useCase(battleId = 1L, optionId = 2L, isPreVote = true)

        assertEquals(SubmitVoteResult.InsufficientPoints, result.getOrNull())
    }

    @Test
    fun `그 외 에러는 실패로 그대로 전파한다`() = runTest {
        val error = RuntimeException("network error")
        coEvery { voteRepository.submitPreVote(1L, 2L) } returns Result.failure(error)

        val result = useCase(battleId = 1L, optionId = 2L, isPreVote = true)

        assertEquals("network error", result.exceptionOrNull()?.message)
    }
}
