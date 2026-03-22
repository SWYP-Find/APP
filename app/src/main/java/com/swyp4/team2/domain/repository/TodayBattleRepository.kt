package com.swyp4.team2.domain.repository

import com.swyp4.team2.domain.model.TodayBattleBoard

interface TodayBattleRepository {
    suspend fun fetchTodayBattles(): Result<TodayBattleBoard>
}
