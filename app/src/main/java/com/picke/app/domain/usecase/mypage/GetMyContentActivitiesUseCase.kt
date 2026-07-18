package com.picke.app.domain.usecase.mypage

import com.picke.app.domain.model.MyContentActivityPage
import com.picke.app.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyContentActivitiesUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int, activityType: String?): Result<MyContentActivityPage> {
        return myPageRepository.getMyContentActivities(offset, size, activityType)
    }
}
