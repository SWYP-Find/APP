package com.picke.app

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SwypApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 앱이 시작될 때 필요한 초기화 작업 후에 추가
        KakaoSdk.init(this, BuildConfig.KAKAO_DEBUG_APPKEY)
        MobileAds.initialize(this) {}
    }
}