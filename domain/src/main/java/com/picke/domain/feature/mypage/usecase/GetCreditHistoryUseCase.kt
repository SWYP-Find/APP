package com.picke.domain.feature.mypage.usecase

import com.picke.domain.feature.mypage.model.CreditHistoryPage
import com.picke.domain.feature.mypage.repository.MyPageRepository

class GetCreditHistoryUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int): Result<CreditHistoryPage> {
        return myPageRepository.getCreditHistory(offset, size)
    }
}
