package com.picke.domain.feature.attendance.usecase

import com.picke.domain.feature.attendance.model.WeeklyAttendance
import com.picke.domain.feature.attendance.repository.AttendanceRepository

class GetWeeklyAttendanceUseCase(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(): Result<WeeklyAttendance> {
        return attendanceRepository.getWeeklyAttendance()
    }
}