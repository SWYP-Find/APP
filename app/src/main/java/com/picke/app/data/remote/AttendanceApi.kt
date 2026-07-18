package com.picke.app.data.remote

import com.picke.app.data.model.AttendanceCheckResponseDto
import com.picke.app.data.model.BaseResponse
import retrofit2.http.POST

interface AttendanceApi {
    @POST("/api/v1/attendance/check")
    suspend fun checkAttendance(): BaseResponse<AttendanceCheckResponseDto>
}
