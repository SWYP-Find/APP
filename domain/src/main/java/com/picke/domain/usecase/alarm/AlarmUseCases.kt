package com.picke.domain.usecase.alarm

data class AlarmUseCases(
    val getAlarmsUseCase: GetAlarmsUseCase,
    val getAlarmDetailUseCase: GetAlarmDetailUseCase,
    val getUnreadAlarmStatusUseCase: GetUnreadAlarmStatusUseCase,
    val readAlarmUseCase: ReadAlarmUseCase,
    val readAllAlarmsUseCase: ReadAllAlarmsUseCase
)