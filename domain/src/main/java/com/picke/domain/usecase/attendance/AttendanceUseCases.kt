package com.picke.domain.usecase.attendance

data class AttendanceUseCases(
    val checkAttendanceUseCase: CheckAttendanceUseCase,
    val getWeeklyAttendanceUseCase: GetWeeklyAttendanceUseCase
)