package com.picke.data.di

import com.picke.data.feature.alarm.datasource.AlarmApi
import com.picke.data.feature.attendance.datasource.AttendanceApi
import com.picke.data.feature.auth.datasource.AuthApi
import com.picke.data.feature.battle.datasource.BattleApi
import com.picke.data.feature.comment.datasource.CommentApi
import com.picke.data.feature.device.datasource.DeviceApi
import com.picke.data.feature.explore.datasource.ExploreApi
import com.picke.data.feature.home.datasource.HomeApi
import com.picke.data.feature.mypage.datasource.MyPageApi
import com.picke.data.feature.perspective.datasource.PerspectiveApi
import com.picke.data.feature.pollquiz.datasource.PollQuizApi
import com.picke.data.feature.proposal.datasource.ProposalApi
import com.picke.data.feature.recommend.datasource.RecommendApi
import com.picke.data.feature.scenario.datasource.ScenarioApi
import com.picke.data.feature.share.datasource.ShareApi
import com.picke.data.feature.todaybattle.datasource.TodayBattleApi
import com.picke.data.feature.vote.datasource.VoteApi
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
    fun provideAuthApi(retrofit: Retrofit): AuthApi {
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

    @Provides
    @Singleton
    fun provideBattleApi(retrofit: Retrofit): BattleApi {
        return retrofit.create(BattleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentApi(retrofit: Retrofit): CommentApi {
        return retrofit.create(CommentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecommendApi(retrofit: Retrofit): RecommendApi {
        return retrofit.create(RecommendApi::class.java)
    }

    @Provides
    @Singleton
    fun provideMyPageApi(retrofit: Retrofit): MyPageApi {
        return retrofit.create(MyPageApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAlarmApi(retrofit: Retrofit): AlarmApi {
        return retrofit.create(AlarmApi::class.java)
    }

    @Provides
    @Singleton
    fun providePollQuizApi(retrofit: Retrofit): PollQuizApi {
        return retrofit.create(PollQuizApi::class.java)
    }

    @Provides
    @Singleton
    fun provideShareApi(retrofit: Retrofit): ShareApi {
        return retrofit.create(ShareApi::class.java)
    }

    @Provides
    @Singleton
    fun provideProposalApi(retrofit: Retrofit): ProposalApi {
        return retrofit.create(ProposalApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDeviceApi(retrofit: Retrofit): DeviceApi {
        return retrofit.create(DeviceApi::class.java)
    }

    @Provides
    @Singleton
    fun provideAttendanceApi(retrofit: Retrofit): AttendanceApi {
        return retrofit.create(AttendanceApi::class.java)
    }
}