package com.picke.app.domain.usecase.todaybattle

import com.picke.app.domain.model.TodayBattleBoard
import com.picke.app.domain.repository.TodayBattleRepository
import javax.inject.Inject

class FetchTodayBattlesUseCase @Inject constructor(
    private val todayBattleRepository: TodayBattleRepository
) {
    suspend operator fun invoke(): Result<TodayBattleBoard> {
        return todayBattleRepository.fetchTodayBattles()
    }
}
