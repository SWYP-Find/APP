package com.picke.app.domain.usecase.attendance

import com.picke.app.domain.model.AttendanceBoard
import com.picke.app.domain.repository.AttendanceRepository
import javax.inject.Inject

class CheckAttendanceUseCase @Inject constructor(
    private val attendanceRepository: AttendanceRepository
) {
    suspend operator fun invoke(): Result<AttendanceBoard> {
        return attendanceRepository.checkAttendance()
    }
}
