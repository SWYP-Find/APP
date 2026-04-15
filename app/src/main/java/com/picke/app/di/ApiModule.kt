package com.picke.app.di

import com.picke.app.data.remote.AlarmApi
import com.picke.app.data.remote.AuthApi
import com.picke.app.data.remote.BattleApi
import com.picke.app.data.remote.CommentApi
import com.picke.app.data.remote.ExploreApi
import com.picke.app.data.remote.HomeApi
import com.picke.app.data.remote.MyPageApi
import com.picke.app.data.remote.PerspectiveApi
import com.picke.app.data.remote.PollQuizApi
import com.picke.app.data.remote.ProposalApi
import com.picke.app.data.remote.RecommendApi
import com.picke.app.data.remote.ScenarioApi
import com.picke.app.data.remote.ShareApi
import com.picke.app.data.remote.TodayBattleApi
import com.picke.app.data.remote.VoteApi
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

    @Provides
    @Singleton
    fun provideBattleApi(retrofit: Retrofit): BattleApi{
        return retrofit.create(BattleApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCommentApi(retrofit: Retrofit): CommentApi{
        return retrofit.create(CommentApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecommendApi(retrofit: Retrofit): RecommendApi{
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
 }