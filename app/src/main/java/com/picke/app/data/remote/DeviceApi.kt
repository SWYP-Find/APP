package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.RegisterDeviceRequest
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Query

interface DeviceApi {
    @POST("/api/v1/devices")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest
    ): BaseResponse<String>

    @DELETE("/api/v1/devices")
    suspend fun deleteDevice(
        @Query("fcmToken") fcmToken: String
    ): BaseResponse<String>
}
