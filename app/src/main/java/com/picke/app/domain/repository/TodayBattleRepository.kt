package com.picke.app.domain.repository

import com.picke.app.domain.model.TodayBattleBoard

interface TodayBattleRepository {
    suspend fun fetchTodayBattles(): Result<TodayBattleBoard>
}
