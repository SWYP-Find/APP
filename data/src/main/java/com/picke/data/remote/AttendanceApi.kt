package com.picke.data.remote

import com.picke.data.model.AttendanceCheckResponseDto
import com.picke.data.model.BaseResponse
import com.picke.data.model.WeeklyAttendanceResponseDto
import retrofit2.http.GET
import retrofit2.http.POST

interface AttendanceApi {
    @POST("/api/v1/attendance/check")
    suspend fun checkAttendance(): BaseResponse<AttendanceCheckResponseDto>

    @GET("/api/v1/attendance/weekly")
    suspend fun getWeeklyAttendance(): BaseResponse<WeeklyAttendanceResponseDto>
}
