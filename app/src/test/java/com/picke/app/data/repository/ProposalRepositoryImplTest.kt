package com.picke.app.data.repository

import com.picke.app.data.remote.ProposalApi
import com.picke.app.domain.exception.NotEnoughPointsException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class ProposalRepositoryImplTest {

    private lateinit var proposalApi: ProposalApi
    private lateinit var repository: ProposalRepositoryImpl

    @Before
    fun setUp() {
        proposalApi = mockk()
        repository = ProposalRepositoryImpl(proposalApi)
    }

    private fun httpException(code: Int) = HttpException(
        Response.error<Any>(code, "{}".toResponseBody("application/json".toMediaTypeOrNull()))
    )

    @Test
    fun `HTTP 400 응답이면 NotEnoughPointsException으로 변환한다`() = runTest {
        coEvery {
            proposalApi.submitProposal(any())
        } throws httpException(400)

        val result = repository.submitProposal("일반", "topic", "A", "B", "desc")

        assertTrue(result.exceptionOrNull() is NotEnoughPointsException)
    }

    @Test
    fun `HTTP 400이 아닌 에러는 그대로 전파한다`() = runTest {
        val error = httpException(500)
        coEvery {
            proposalApi.submitProposal(any())
        } throws error

        val result = repository.submitProposal("일반", "topic", "A", "B", "desc")

        assertTrue(result.exceptionOrNull() === error)
    }
}
