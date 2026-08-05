package com.picke.domain.feature.mypage.usecase

import com.picke.domain.feature.mypage.model.MyRecapBoard
import com.picke.domain.feature.mypage.repository.MyPageRepository

class GetMyRecapUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<MyRecapBoard> {
        return myPageRepository.getMyRecap()
    }
}
