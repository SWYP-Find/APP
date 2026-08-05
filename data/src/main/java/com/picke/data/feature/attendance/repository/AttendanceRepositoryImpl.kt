package com.picke.data.feature.attendance.repository

import android.util.Log
import com.picke.data.common.model.toResult
import com.picke.data.feature.attendance.datasource.AttendanceApi
import com.picke.data.feature.attendance.model.toDomain
import com.picke.domain.feature.attendance.model.AttendanceBoard
import com.picke.domain.feature.attendance.model.WeeklyAttendance
import com.picke.domain.feature.attendance.repository.AttendanceRepository
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AttendanceRepositoryImpl"

@Singleton
class AttendanceRepositoryImpl @Inject constructor(
    private val attendanceApi: AttendanceApi
) : AttendanceRepository {

    override suspend fun checkAttendance(): Result<AttendanceBoard> {
        return try {
            Log.d(TAG, "[API_REQ] POST /api/v1/attendance/check 호출")
            attendanceApi.checkAttendance()
                .toResult("출석 체크에 실패했습니다.")
                .map { dto ->
                    Log.d(TAG, "[API_RES] 출석 체크 응답 수신: $dto")
                    dto.toDomain()
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 출석 체크 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getWeeklyAttendance(): Result<WeeklyAttendance> {
        return try {
            Log.d(TAG, "[API_REQ] GET /api/v1/attendance/weekly 호출")
            attendanceApi.getWeeklyAttendance()
                .toResult("이번 주 출석 현황 조회에 실패했습니다.")
                .map { dto ->
                    Log.d(TAG, "[API_RES] 이번 주 출석 현황 응답 수신: $dto")
                    dto.toDomain()
                }
        } catch (e: Exception) {
            Log.e(TAG, "[API_ERR] 이번 주 출석 현황 조회 예외 발생: ${e.message}")
            Result.failure(e)
        }
    }
}
