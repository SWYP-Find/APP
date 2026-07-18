package com.picke.app.domain.usecase.mypage

import com.picke.app.domain.model.MyBattleRecordPage
import com.picke.app.domain.repository.MyPageRepository
import javax.inject.Inject

class GetMyBattleRecordsUseCase @Inject constructor(
    private val myPageRepository: MyPageRepository
) {
    suspend operator fun invoke(offset: Int?, size: Int, voteSide: String?): Result<MyBattleRecordPage> {
        return myPageRepository.getMyBattleRecords(offset, size, voteSide)
    }
}
