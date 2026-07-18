package com.picke.app.domain.usecase

import com.picke.app.domain.model.MyPageInfoBoard
import com.picke.app.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyPageInfoUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<MyPageInfoBoard> {
        return myPageRepository.getMyPageInfo()
    }
}
