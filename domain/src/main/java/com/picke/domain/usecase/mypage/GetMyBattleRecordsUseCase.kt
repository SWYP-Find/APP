package com.picke.domain.usecase.mypage

import com.picke.domain.model.MyBattleRecordPage
import com.picke.domain.repository.MyPageRepository

class GetMyBattleRecordsUseCase(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage> {
        return myPageRepository.getMyBattleRecords(offset, size, voteSide)
    }
}
