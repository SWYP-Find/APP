package com.picke.data.repository

import com.picke.data.model.toDomainModel
import com.picke.data.model.toResult
import com.picke.data.remote.TodayBattleApi
import com.picke.domain.model.TodayBattleBoard
import com.picke.domain.repository.TodayBattleRepository
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