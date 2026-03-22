package com.swyp4.team2.di

import com.swyp4.team2.data.repository.AuthRepositoryImpl
import com.swyp4.team2.data.repository.ExploreRepositoryImpl
import com.swyp4.team2.data.repository.HomeRepositoryImpl
import com.swyp4.team2.data.repository.ScenarioRepositoryImpl
import com.swyp4.team2.data.repository.TodayBattleRepositoryImpl
import com.swyp4.team2.domain.repository.AuthRepository
import com.swyp4.team2.domain.repository.ExploreRepository
import com.swyp4.team2.domain.repository.HomeRepository
import com.swyp4.team2.domain.repository.ScenarioRepository
import com.swyp4.team2.domain.repository.TodayBattleRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindExploreRepository(
        exploreRepositoryImpl: ExploreRepositoryImpl
    ): ExploreRepository

    @Binds
    @Singleton
    abstract fun bindHomeRepository(
        homeRepositoryImpl: HomeRepositoryImpl
    ): HomeRepository

    @Binds
    @Singleton
    abstract fun bindTodayBattleRepository(
        todayBattleRepositoryImpl: TodayBattleRepositoryImpl
    ): TodayBattleRepository

    @Binds
    @Singleton
    abstract fun bindScenarioRepository(
        scenarioRepositoryImpl: ScenarioRepositoryImpl
    ): ScenarioRepository
}