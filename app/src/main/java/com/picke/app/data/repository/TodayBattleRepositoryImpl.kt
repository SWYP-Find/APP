package com.picke.app.data.repository

import com.picke.app.data.model.toResult
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.remote.TodayBattleApi
import com.picke.app.domain.model.TodayBattleBoard
import com.picke.app.domain.repository.TodayBattleRepository
import javax.inject.Inject

class TodayBattleRepositoryImpl @Inject constructor(
    private val battleApi: TodayBattleApi
) : TodayBattleRepository {
    override suspend fun fetchTodayBattles(): Result<TodayBattleBoard> {
        return try {
            battleApi.getTodayBattles()
                .toResult("오늘의 배틀을 불러오는데 실패했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}