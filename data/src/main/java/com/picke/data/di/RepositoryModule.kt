package com.picke.data.di

import com.picke.data.repository.AlarmRepositoryImpl
import com.picke.data.repository.AttendanceRepositoryImpl
import com.picke.data.repository.AuthRepositoryImpl
import com.picke.data.repository.BattleRepositoryImpl
import com.picke.data.repository.CommentRepositoryImpl
import com.picke.data.repository.DeviceRepositoryImpl
import com.picke.data.repository.ExploreRepositoryImpl
import com.picke.data.repository.HomeRepositoryImpl
import com.picke.data.repository.LocalPreferencesRepositoryImpl
import com.picke.data.repository.MyPageRepositoryImpl
import com.picke.data.repository.PerspectiveRepositoryImpl
import com.picke.data.repository.PollQuizRepositoryImpl
import com.picke.data.repository.ProposalRepositoryImpl
import com.picke.data.repository.RecommendRepositoryImpl
import com.picke.data.repository.ScenarioRepositoryImpl
import com.picke.data.repository.ShareRepositoryImpl
import com.picke.data.repository.TodayBattleRepositoryImpl
import com.picke.data.repository.VoteRepositoryImpl
import com.picke.domain.repository.AlarmRepository
import com.picke.domain.repository.AttendanceRepository
import com.picke.domain.repository.AuthRepository
import com.picke.domain.repository.BattleRepository
import com.picke.domain.repository.CommentRepository
import com.picke.domain.repository.DeviceRepository
import com.picke.domain.repository.ExploreRepository
import com.picke.domain.repository.HomeRepository
import com.picke.domain.repository.LocalPreferencesRepository
import com.picke.domain.repository.MyPageRepository
import com.picke.domain.repository.PerspectiveRepository
import com.picke.domain.repository.PollQuizRepository
import com.picke.domain.repository.ProposalRepository
import com.picke.domain.repository.RecommendRepository
import com.picke.domain.repository.ScenarioRepository
import com.picke.domain.repository.ShareRepository
import com.picke.domain.repository.TodayBattleRepository
import com.picke.domain.repository.VoteRepository
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

    @Binds
    @Singleton
    abstract fun bindProposalRepository(
        proposalRepositoryImpl: ProposalRepositoryImpl
    ): ProposalRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindAttendanceRepository(
        attendanceRepositoryImpl: AttendanceRepositoryImpl
    ): AttendanceRepository

    @Binds
    @Singleton
    abstract fun bindLocalPreferencesRepository(
        impl: LocalPreferencesRepositoryImpl
    ): LocalPreferencesRepository
}