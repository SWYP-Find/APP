package com.picke.app.data.repository

import android.util.Log
import com.picke.app.data.model.toDomain
import com.picke.app.data.model.toResult
import com.picke.app.data.remote.AttendanceApi
import com.picke.app.domain.model.AttendanceBoard
import com.picke.app.domain.repository.AttendanceRepository
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
}
