package com.picke.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.lifecycle.ProcessLifecycleOwner
// AdMob 미사용으로 SDK import 비활성화 (추후 재사용 예정)
// import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.KakaoSdk
import com.picke.app.di.AdMobManager
import com.picke.app.notification.FCMService
import com.picke.app.util.AppLifecycleObserver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SwypApplication : Application() {

    @Inject
    lateinit var adMobManager: AdMobManager

    @Inject
    lateinit var appLifecycleObserver: AppLifecycleObserver

    override fun onCreate() {
        super.onCreate()
        KakaoSdk.init(this, BuildConfig.KAKAO_DEBUG_APPKEY)
        // AdMob SDK 초기화 비활성화 (추후 재사용 예정)
        // MobileAds.initialize(this) { adMobManager.onMobileAdsInitialized() }
        createNotificationChannel()
        ProcessLifecycleOwner.get().lifecycle.addObserver(appLifecycleObserver)
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