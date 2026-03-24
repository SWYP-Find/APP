package com.swyp4.team2.di

import com.swyp4.team2.data.remote.AuthApi
import com.swyp4.team2.data.remote.ExploreApi
import com.swyp4.team2.data.remote.HomeApi
import com.swyp4.team2.data.remote.PerspectiveApi
import com.swyp4.team2.data.remote.ScenarioApi
import com.swyp4.team2.data.remote.TodayBattleApi
import com.swyp4.team2.data.remote.VoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi{
        return retrofit.create(AuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExploreApi(retrofit: Retrofit): ExploreApi {
        return retrofit.create(ExploreApi::class.java)
    }

    @Provides
    @Singleton
    fun provideHomeApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTodayBattleApi(retrofit: Retrofit): TodayBattleApi {
        return retrofit.create(TodayBattleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideScenarioApi(retrofit: Retrofit): ScenarioApi {
        return retrofit.create(ScenarioApi::class.java)
    }

    @Provides
    @Singleton
    fun provideVoteApi(retrofit: Retrofit): VoteApi {
        return retrofit.create(VoteApi::class.java)
    }

    @Provides
    @Singleton
    fun providePerspectiveApi(retrofit: Retrofit): PerspectiveApi {
        return retrofit.create(PerspectiveApi::class.java)
    }
}