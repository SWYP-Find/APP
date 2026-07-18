package com.picke.app.domain.usecase.auth

import com.picke.app.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WithdrawUseCaseTest {

    @Test
    fun `한글 탈퇴 사유를 서버 코드로 매핑하여 전달한다`() = runTest {
        val cases = mapOf(
            "자주 이용하지 않아요" to "NOT_USED_OFTEN",
            "보고 싶은 배틀 주제가 없어요" to "NO_INTERESTING_BATTLES",
            "배틀 방식이 제게 잘 맞지 않아요" to "BATTLE_STYLE_NOT_FIT",
            "서비스 이용이 불편해요" to "SERVICE_INCONVENIENT",
            "이용할 시간이 없어요" to "NO_TIME",
            "기타" to "OTHER",
            "알 수 없는 사유" to "OTHER"
        )

        cases.forEach { (korean, code) ->
            val authRepository: AuthRepository = mockk()
            coEvery { authRepository.withdraw(any()) } returns Result.success(Unit)

            WithdrawUseCase(authRepository)(korean)

            coVerify(exactly = 1) { authRepository.withdraw(code) }
        }
    }
}
