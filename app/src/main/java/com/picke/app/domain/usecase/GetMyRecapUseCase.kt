package com.picke.app.domain.usecase

import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyRecapUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(): Result<MyRecapBoard> {
        return myPageRepository.getMyRecap()
    }
}
