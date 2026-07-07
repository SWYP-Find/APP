package com.picke.app.di

import android.content.Context
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.picke.app.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MixpanelModule {

    @Provides
    @Singleton
    fun provideMixpanel(@ApplicationContext context: Context): MixpanelAPI {
        val projectToken = BuildConfig.MIXPANEL_PROJECT_TOKEN

        // trackAutomaticEvents=true: 세션/설치/업데이트 자동 이벤트 (계약서 §0 - 양 플랫폼 통일)
        return MixpanelAPI.getInstance(context, projectToken, true)
    }
}