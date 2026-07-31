package com.picke.domain.usecase.mypage

import com.picke.domain.model.CreditHistoryPage
import com.picke.domain.repository.MyPageRepository

class GetCreditHistoryUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int): Result<CreditHistoryPage> {
        return myPageRepository.getCreditHistory(offset, size)
    }
}
