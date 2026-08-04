package com.picke.data.feature.attendance.datasource

import com.picke.data.common.model.BaseResponse
import com.picke.data.feature.attendance.model.AttendanceCheckResponseDto
import com.picke.data.feature.attendance.model.WeeklyAttendanceResponseDto
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceApi {
    @POST("/api/v1/attendance/check")
    suspend fun checkAttendance(): BaseResponse<AttendanceCheckResponseDto>

    @GET("/api/v1/attendance/weekly")
    suspend fun getWeeklyAttendance(): BaseResponse<WeeklyAttendanceResponseDto>
}