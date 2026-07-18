package com.picke.app.domain.usecase.pollquiz

import com.picke.app.domain.model.PollQuizVoteBoard
import com.picke.app.domain.repository.PollQuizRepository
import com.picke.app.util.ContentType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class SubmitTodayPickVoteUseCaseTest {

    private lateinit var pollQuizRepository: PollQuizRepository
    private lateinit var useCase: SubmitTodayPickVoteUseCase

    @Before
    fun setUp() {
        pollQuizRepository = mockk()
        useCase = SubmitTodayPickVoteUseCase(pollQuizRepository)
    }

    @Test
    fun `type이 QUIZ이면 퀴즈 투표를 제출한다`() = runTest {
        val expected = PollQuizVoteBoard(battleId = 1L, selectedOptionId = 2L, totalCount = 10, stats = emptyList())
        coEvery { pollQuizRepository.submitQuizVote(1L, 2L) } returns Result.success(expected)

        val result = useCase(battleId = 1L, optionId = 2L, type = ContentType.QUIZ)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { pollQuizRepository.submitQuizVote(1L, 2L) }
        coVerify(exactly = 0) { pollQuizRepository.submitPollVote(any(), any()) }
    }

    @Test
    fun `type이 QUIZ가 아니면 투표(폴)를 제출한다`() = runTest {
        val expected = PollQuizVoteBoard(battleId = 1L, selectedOptionId = 2L, totalCount = 10, stats = emptyList())
        coEvery { pollQuizRepository.submitPollVote(1L, 2L) } returns Result.success(expected)

        val result = useCase(battleId = 1L, optionId = 2L, type = ContentType.POLL)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { pollQuizRepository.submitPollVote(1L, 2L) }
        coVerify(exactly = 0) { pollQuizRepository.submitQuizVote(any(), any()) }
    }
}
