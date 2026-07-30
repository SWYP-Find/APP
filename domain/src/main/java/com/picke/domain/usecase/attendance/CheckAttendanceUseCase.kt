package com.picke.domain.usecase.attendance

import com.picke.domain.model.AttendanceBoard
import com.picke.domain.model.WeeklyAttendance
import com.picke.domain.repository.AttendanceRepository

class CheckAttendanceUseCase(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(): Result<AttendanceBoard> {
        return attendanceRepository.checkAttendance()
    }
}

class GetWeeklyAttendanceUseCase(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(): Result<WeeklyAttendance> {
        return attendanceRepository.getWeeklyAttendance()
    }
}
