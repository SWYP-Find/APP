package com.picke.domain.feature.attendance.usecase

data class AttendanceUseCases(
    val checkAttendanceUseCase: CheckAttendanceUseCase,
    val getWeeklyAttendanceUseCase: GetWeeklyAttendanceUseCase
)