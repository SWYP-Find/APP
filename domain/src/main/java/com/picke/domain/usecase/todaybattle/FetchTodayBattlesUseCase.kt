package com.picke.domain.usecase.todaybattle

import com.picke.domain.model.TodayBattleBoard
import com.picke.domain.repository.TodayBattleRepository

class FetchTodayBattlesUseCase(
    private val todayBattleRepository: TodayBattleRepository
) {
    suspend operator fun invoke(): Result<TodayBattleBoard> {
        return todayBattleRepository.fetchTodayBattles()
    }
}
