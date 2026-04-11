package com.picke.app.di

import com.picke.app.data.repository.AlarmRepositoryImpl
import com.picke.app.data.repository.AuthRepositoryImpl
import com.picke.app.data.repository.BattleRepositoryImpl
import com.picke.app.data.repository.CommentRepositoryImpl
import com.picke.app.data.repository.ExploreRepositoryImpl
import com.picke.app.data.repository.HomeRepositoryImpl
import com.picke.app.data.repository.MyPageRepositoryImpl
import com.picke.app.data.repository.PerspectiveRepositoryImpl
import com.picke.app.data.repository.PollQuizRepositoryImpl
import com.picke.app.data.repository.RecommendRepositoryImpl
import com.picke.app.data.repository.ScenarioRepositoryImpl
import com.picke.app.data.repository.ShareRepositoryImpl
import com.picke.app.data.repository.TodayBattleRepositoryImpl
import com.picke.app.data.repository.VoteRepositoryImpl
import com.picke.app.domain.repository.AlarmRepository
import com.picke.app.domain.repository.AuthRepository
import com.picke.app.domain.repository.BattleRepository
import com.picke.app.domain.repository.CommentRepository
import com.picke.app.domain.repository.ExploreRepository
import com.picke.app.domain.repository.HomeRepository
import com.picke.app.domain.repository.MyPageRepository
import com.picke.app.domain.repository.PerspectiveRepository
import com.picke.app.domain.repository.PollQuizRepository
import com.picke.app.domain.repository.RecommendRepository
import com.picke.app.domain.repository.ScenarioRepository
import com.picke.app.domain.repository.ShareRepository
import com.picke.app.domain.repository.TodayBattleRepository
import com.picke.app.domain.repository.VoteRepository
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

    @Binds
    @Singleton
    abstract fun bindVoteRepository(
        voteRepositoryImpl: VoteRepositoryImpl
    ): VoteRepository

    @Binds
    @Singleton
    abstract fun bindPerspectiveRepository(
        perspectiveRepositoryImpl: PerspectiveRepositoryImpl
    ): PerspectiveRepository

    @Binds
    @Singleton
    abstract fun bindBattleRepository(
        battleRepositoryImpl: BattleRepositoryImpl
    ): BattleRepository

    @Binds
    @Singleton
    abstract fun bindCommentRepository(
        commentRepositoryImpl: CommentRepositoryImpl
    ): CommentRepository

    @Binds
    @Singleton
    abstract fun bindRecommendRepository(
        recommendRepositoryImpl: RecommendRepositoryImpl
    ): RecommendRepository

    @Binds
    @Singleton
    abstract fun bindMyPageRepository(
        myPageRepositoryImpl: MyPageRepositoryImpl
    ): MyPageRepository

    @Binds
    @Singleton
    abstract fun bindAlarmRepository(
        alarmRepositoryImpl: AlarmRepositoryImpl
    ): AlarmRepository

    @Binds
    @Singleton
    abstract fun bindPollQuizRepository(
        pollQuizRepositoryImpl: PollQuizRepositoryImpl
    ): PollQuizRepository

    @Binds
    @Singleton
    abstract fun bindShareRepository(
        shareRepositoryImpl: ShareRepositoryImpl
    ): ShareRepository
}