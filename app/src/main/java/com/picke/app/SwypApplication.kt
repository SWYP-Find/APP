package com.picke.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.KakaoSdk
import com.picke.app.notification.FCMService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SwypApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_DEBUG_APPKEY)
        MobileAds.initialize(this) {}
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                FCMService.CHANNEL_ID,
                "픽케 알림",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "픽케 배틀, 관점, 공지 알림"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}