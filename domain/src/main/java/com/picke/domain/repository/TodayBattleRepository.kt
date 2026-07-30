package com.picke.domain.repository

import com.picke.domain.model.TodayBattleBoard

interface TodayBattleRepository {
    suspend fun fetchTodayBattles(): Result<TodayBattleBoard>
}
