package com.picke.data.remote

import com.picke.data.model.BaseResponse
import com.picke.data.model.RegisterDeviceRequest
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
