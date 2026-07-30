package com.picke.domain.usecase.mypage

import com.picke.domain.model.MyPageInfoBoard
import com.picke.domain.repository.MyPageRepository

class GetMyPageInfoUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<MyPageInfoBoard> {
        return myPageRepository.getMyPageInfo()
    }
}
