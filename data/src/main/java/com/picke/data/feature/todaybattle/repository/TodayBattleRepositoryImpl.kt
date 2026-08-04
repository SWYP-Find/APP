package com.picke.data.feature.todaybattle.repository

import com.picke.data.common.model.toResult
import com.picke.data.feature.todaybattle.datasource.TodayBattleApi
import com.picke.data.feature.todaybattle.model.toDomainModel
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