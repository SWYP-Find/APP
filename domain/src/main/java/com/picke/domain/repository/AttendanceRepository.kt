package com.picke.domain.repository

import com.picke.domain.model.AttendanceBoard
import com.picke.domain.model.WeeklyAttendance

interface AttendanceRepository {
    /**
     * 출석 체크 API (하루 1회, 자정 이후 최초 진입 시 호출)
     *
     * @return 지급된 포인트 및 연속 출석 정보를 담은 [AttendanceBoard]
     */
    suspend fun checkAttendance(): Result<AttendanceBoard>

    /**
     * 이번 주(월~일) 요일별 출석 상태와 연속 출석 정보를 조회하는 API
     *
     * @return 이번 주 출석 현황을 담은 [WeeklyAttendance]
     */
    suspend fun getWeeklyAttendance(): Result<WeeklyAttendance>
}
