package com.picke.app.domain.repository

import com.picke.app.domain.model.AttendanceBoard

interface AttendanceRepository {
    /**
     * 출석 체크 API (하루 1회, 자정 이후 최초 진입 시 호출)
     *
     * @return 지급된 포인트 및 연속 출석 정보를 담은 [AttendanceBoard]
     */
    suspend fun checkAttendance(): Result<AttendanceBoard>
}
