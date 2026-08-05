package com.picke.domain.feature.todaybattle.repository

import com.picke.domain.feature.todaybattle.model.TodayBattleBoard

interface TodayBattleRepository {
    suspend fun fetchTodayBattles(): Result<TodayBattleBoard>
}