package com.swyp4.team2.data.repository

import com.swyp4.team2.data.model.toDomainModel
import com.swyp4.team2.data.remote.AlarmApi
import com.swyp4.team2.domain.model.AlarmPageBoard
import com.swyp4.team2.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmApi: AlarmApi
) : AlarmRepository {

    override suspend fun getAlarms(category: String, page: Int, size: Int): Result<AlarmPageBoard> {
        return try {
            val response = alarmApi.getAlarms(category, page, size)
            val data = response.data

            if (response.statusCode == 0 && data != null) {
                Result.success(data.toDomainModel())
            } else {
                val errorMessage = response.error?.message ?: "알림 목록을 불러오지 못했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readAlarm(notificationId: Long): Result<String> {
        return try {
            val response = alarmApi.readAlarm(notificationId)

            if (response.statusCode == 0) {
                Result.success(response.data ?: "Success")
            } else {
                val errorMessage = response.error?.message ?: "알림 읽음 처리에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readAllAlarms(): Result<String> {
        return try {
            val response = alarmApi.readAllAlarms()

            if (response.statusCode == 0) {
                Result.success(response.data ?: "Success")
            } else {
                val errorMessage = response.error?.message ?: "알림 전체 읽음 처리에 실패했습니다."
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}