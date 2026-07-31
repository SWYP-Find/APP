package com.picke.domain.usecase.mypage

import com.picke.domain.model.MyContentActivityPage
import com.picke.domain.repository.MyPageRepository

class GetMyContentActivitiesUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage> {
        return myPageRepository.getMyContentActivities(offset, size, activityType)
    }
}
