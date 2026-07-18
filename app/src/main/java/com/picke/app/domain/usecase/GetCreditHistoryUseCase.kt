package com.picke.app.domain.usecase

import com.picke.app.domain.model.CreditHistoryPage
import com.picke.app.domain.repository.MyPageRepository
import javax.inject.Inject

class GetCreditHistoryUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int): Result<CreditHistoryPage> {
        return myPageRepository.getCreditHistory(offset, size)
    }
}
