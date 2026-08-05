package com.picke.data.feature.device.model

import com.google.gson.annotations.SerializedName

data class RegisterDeviceRequest(
    @SerializedName("fcmToken")
    val fcmToken: String,
    @SerializedName("platform")
    val platform: String
)
