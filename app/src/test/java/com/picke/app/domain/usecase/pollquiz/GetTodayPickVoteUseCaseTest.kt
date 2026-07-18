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

class GetTodayPickVoteUseCaseTest {

    private lateinit var pollQuizRepository: PollQuizRepository
    private lateinit var useCase: GetTodayPickVoteUseCase

    @Before
    fun setUp() {
        pollQuizRepository = mockk()
        useCase = GetTodayPickVoteUseCase(pollQuizRepository)
    }

    @Test
    fun `type이 QUIZ이면 내 퀴즈 투표 내역을 조회한다`() = runTest {
        val expected = PollQuizVoteBoard(battleId = 1L, selectedOptionId = 2L, totalCount = 10, stats = emptyList())
        coEvery { pollQuizRepository.getMyQuizVote(1L) } returns Result.success(expected)

        val result = useCase(battleId = 1L, type = ContentType.QUIZ)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { pollQuizRepository.getMyQuizVote(1L) }
        coVerify(exactly = 0) { pollQuizRepository.getMyPollVote(any()) }
    }

    @Test
    fun `type이 QUIZ가 아니면 내 투표(폴) 내역을 조회한다`() = runTest {
        val expected = PollQuizVoteBoard(battleId = 1L, selectedOptionId = 2L, totalCount = 10, stats = emptyList())
        coEvery { pollQuizRepository.getMyPollVote(1L) } returns Result.success(expected)

        val result = useCase(battleId = 1L, type = ContentType.POLL)

        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { pollQuizRepository.getMyPollVote(1L) }
        coVerify(exactly = 0) { pollQuizRepository.getMyQuizVote(any()) }
    }
}
