package com.picke.domain.feature.mypage.usecase

import com.picke.domain.feature.mypage.model.MyPageInfoBoard
import com.picke.domain.feature.mypage.repository.MyPageRepository

class GetMyPageInfoUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<MyPageInfoBoard> {
        return myPageRepository.getMyPageInfo()
    }
}
