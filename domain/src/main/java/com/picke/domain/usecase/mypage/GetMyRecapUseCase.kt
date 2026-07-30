package com.picke.domain.usecase.mypage

import com.picke.domain.model.MyRecapBoard
import com.picke.domain.repository.MyPageRepository

class GetMyRecapUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<MyRecapBoard> {
        return myPageRepository.getMyRecap()
    }
}
