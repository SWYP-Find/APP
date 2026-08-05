package com.picke.domain.feature.mypage.usecase

import com.picke.domain.feature.mypage.model.MyContentActivityPage
import com.picke.domain.feature.mypage.repository.MyPageRepository

class GetMyContentActivitiesUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage> {
        return myPageRepository.getMyContentActivities(offset, size, activityType)
    }
}
