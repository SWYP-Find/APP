package com.picke.app.domain.usecase

import com.picke.app.domain.repository.AuthRepository
import javax.inject.Inject

class WithdrawUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(selectedKoreanReason: String): Result<Unit> {
        return authRepository.withdraw(reason = toReasonCode(selectedKoreanReason))
    }

    private fun toReasonCode(selectedKoreanReason: String): String = when (selectedKoreanReason) {
        "자주 이용하지 않아요" -> "NOT_USED_OFTEN"
        "보고 싶은 배틀 주제가 없어요" -> "NO_INTERESTING_BATTLES"
        "배틀 방식이 제게 잘 맞지 않아요" -> "BATTLE_STYLE_NOT_FIT"
        "서비스 이용이 불편해요" -> "SERVICE_INCONVENIENT"
        "이용할 시간이 없어요" -> "NO_TIME"
        "기타" -> "OTHER"
        else -> "OTHER"
    }
}
