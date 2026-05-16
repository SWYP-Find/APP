package com.picke.app.data.repository

import com.picke.app.data.model.AlarmDetailBoard
import com.picke.app.data.model.toDomainModel
import com.picke.app.data.model.toResult
import com.picke.app.data.remote.AlarmApi
import com.picke.app.domain.model.AlarmPageBoard
import com.picke.app.domain.repository.AlarmRepository
import javax.inject.Inject

class AlarmRepositoryImpl @Inject constructor(
    private val alarmApi: AlarmApi
) : AlarmRepository {

    override suspend fun getAlarms(category: String, page: Int, size: Int): Result<AlarmPageBoard> {
        return try {
            alarmApi.getAlarms(category, page, size)
                .toResult("알림 목록을 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getAlarmDetail(notificationId: Long): Result<AlarmDetailBoard> {
        return try {
            alarmApi.getAlarmDetail(notificationId)
                .toResult("알림 상세 정보를 불러오지 못했습니다.")
                .map { it.toDomainModel() }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun readAlarm(notificationId: Long): Result<String> {
        return try {
            val response = alarmApi.readAlarm(notificationId)

            if (response.statusCode == 200) {
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

            if (response.statusCode == 200) {
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