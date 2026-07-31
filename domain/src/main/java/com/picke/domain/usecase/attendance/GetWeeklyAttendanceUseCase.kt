package com.picke.domain.usecase.attendance

import com.picke.domain.model.WeeklyAttendance
import com.picke.domain.repository.AttendanceRepository

class GetWeeklyAttendanceUseCase(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(): Result<WeeklyAttendance> {
        return attendanceRepository.getWeeklyAttendance()
    }
}