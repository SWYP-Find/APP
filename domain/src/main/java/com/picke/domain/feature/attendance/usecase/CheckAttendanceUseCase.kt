package com.picke.domain.feature.attendance.usecase

import com.picke.domain.feature.attendance.model.AttendanceBoard
import com.picke.domain.feature.attendance.repository.AttendanceRepository

class CheckAttendanceUseCase(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(): Result<AttendanceBoard> {
        return attendanceRepository.checkAttendance()
    }
}