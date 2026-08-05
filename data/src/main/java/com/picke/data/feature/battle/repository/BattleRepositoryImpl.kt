package com.picke.data.feature.battle.repository

import com.picke.data.common.model.toResult
import com.picke.data.feature.battle.datasource.BattleApi
import com.picke.data.feature.battle.model.toDomainModel
import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.model.BattleStatusBoard
import com.picke.domain.feature.battle.repository.BattleRepository
import javax.inject.Inject

class BattleRepositoryImpl @Inject constructor(
    private val battleApi: BattleApi
) : BattleRepository {

    override suspend fun getBattleDetail(battleId: Long): Result<BattleDetailBoard> {
        return try {
            battleApi.getBattleDetail(battleId)
                .toResult("배틀 상세 정보를 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBattleStatus(battleId: Long): Result<BattleStatusBoard> {
        return try {
            battleApi.getBattleStatus(battleId)
                .toResult("배틀 진행 상태를 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}