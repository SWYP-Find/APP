package com.picke.domain.feature.mypage.usecase

import com.picke.domain.feature.mypage.model.MyBattleRecordPage
import com.picke.domain.feature.mypage.repository.MyPageRepository

class GetMyBattleRecordsUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage> {
        return myPageRepository.getMyBattleRecords(offset, size, voteSide)
    }
}
