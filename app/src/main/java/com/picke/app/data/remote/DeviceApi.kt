package com.picke.app.data.remote

import com.picke.app.data.model.BaseResponse
import com.picke.app.data.model.RegisterDeviceRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface DeviceApi {
    @POST("/api/v1/devices")
    suspend fun registerDevice(
        @Body request: RegisterDeviceRequest
    ): BaseResponse<String>
}
