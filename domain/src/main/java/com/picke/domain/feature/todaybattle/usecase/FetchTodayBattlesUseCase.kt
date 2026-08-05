package com.picke.domain.feature.todaybattle.usecase

import com.picke.domain.feature.todaybattle.model.TodayBattleBoard
import com.picke.domain.feature.todaybattle.repository.TodayBattleRepository

class FetchTodayBattlesUseCase(
    private val todayBattleRepository: TodayBattleRepository
) {
    suspend operator fun invoke(): Result<TodayBattleBoard> {
        return todayBattleRepository.fetchTodayBattles()
    }
}
