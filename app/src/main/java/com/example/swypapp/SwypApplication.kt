package com.example.swypapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SwypApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 앱이 시작될 때 필요한 초기화 작업 후에 추가
    }
}

