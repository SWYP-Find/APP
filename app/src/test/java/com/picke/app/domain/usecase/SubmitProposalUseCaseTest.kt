package com.picke.app.domain.usecase

import com.picke.app.domain.model.ProposalBoard
import com.picke.app.domain.repository.ProposalRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class SubmitProposalUseCaseTest {

    private lateinit var proposalRepository: ProposalRepository
    private lateinit var useCase: SubmitProposalUseCase

    @Before
    fun setUp() {
        proposalRepository = mockk()
        useCase = SubmitProposalUseCase(proposalRepository)
    }

    @Test
    fun `제안이 성공하면 Success를 반환한다`() = runTest {
        coEvery {
            proposalRepository.submitProposal(any(), any(), any(), any(), any())
        } returns Result.success(
            ProposalBoard(
                id = 10L,
                userId = 1L,
                nickname = "nick",
                category = "일반",
                topic = "topic",
                positionA = "A",
                positionB = "B",
                description = "desc",
                status = "PENDING",
                createdAt = "2026-07-18"
            )
        )

        val result = useCase("일반", "topic", "A", "B", "desc")

        assertEquals(SubmitProposalResult.Success(10L), result.getOrNull())
    }

    @Test
    fun `HTTP 400 에러이면 NotEnoughPoints로 변환한다`() = runTest {
        val httpException = HttpException(
            Response.error<Any>(400, "{}".toResponseBody("application/json".toMediaTypeOrNull()))
        )
        coEvery {
            proposalRepository.submitProposal(any(), any(), any(), any(), any())
        } returns Result.failure(httpException)

        val result = useCase("일반", "topic", "A", "B", "desc")

        assertEquals(SubmitProposalResult.NotEnoughPoints, result.getOrNull())
    }

    @Test
    fun `그 외 에러는 실패로 그대로 전파한다`() = runTest {
        val error = RuntimeException("network error")
        coEvery {
            proposalRepository.submitProposal(any(), any(), any(), any(), any())
        } returns Result.failure(error)

        val result = useCase("일반", "topic", "A", "B", "desc")

        assertTrue(result.isFailure)
        assertEquals("network error", result.exceptionOrNull()?.message)
    }
}
