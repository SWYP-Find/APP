package com.picke.app.di

import com.picke.domain.feature.alarm.repository.AlarmRepository
import com.picke.domain.feature.attendance.repository.AttendanceRepository
import com.picke.domain.feature.auth.repository.AuthRepository
import com.picke.domain.feature.battle.repository.BattleRepository
import com.picke.domain.feature.comment.repository.CommentRepository
import com.picke.domain.feature.device.repository.DeviceRepository
import com.picke.domain.feature.explore.repository.ExploreRepository
import com.picke.domain.feature.home.repository.HomeRepository
import com.picke.domain.common.local.LocalPreferencesRepository
import com.picke.domain.feature.mypage.repository.MyPageRepository
import com.picke.domain.feature.perspective.repository.PerspectiveRepository
import com.picke.domain.feature.pollquiz.repository.PollQuizRepository
import com.picke.domain.feature.proposal.repository.ProposalRepository
import com.picke.domain.feature.recommend.repository.RecommendRepository
import com.picke.domain.feature.scenario.repository.ScenarioRepository
import com.picke.domain.feature.share.repository.ShareRepository
import com.picke.domain.feature.todaybattle.repository.TodayBattleRepository
import com.picke.domain.feature.vote.repository.VoteRepository
import com.picke.domain.feature.alarm.usecase.AlarmUseCases
import com.picke.domain.feature.alarm.usecase.GetAlarmDetailUseCase
import com.picke.domain.feature.alarm.usecase.GetAlarmsUseCase
import com.picke.domain.feature.alarm.usecase.GetUnreadAlarmStatusUseCase
import com.picke.domain.feature.alarm.usecase.ReadAlarmUseCase
import com.picke.domain.feature.alarm.usecase.ReadAllAlarmsUseCase
import com.picke.domain.feature.attendance.usecase.AttendanceUseCases
import com.picke.domain.feature.attendance.usecase.CheckAttendanceUseCase
import com.picke.domain.feature.attendance.usecase.GetWeeklyAttendanceUseCase
import com.picke.domain.feature.auth.usecase.AuthUseCases
import com.picke.domain.feature.auth.usecase.LoginUseCase
import com.picke.domain.feature.auth.usecase.LogoutUseCase
import com.picke.domain.feature.auth.usecase.RefreshAccessTokenUseCase
import com.picke.domain.feature.auth.usecase.WithdrawUseCase
import com.picke.domain.feature.battle.usecase.BattleUseCases
import com.picke.domain.feature.battle.usecase.GetBattleDetailUseCase
import com.picke.domain.feature.battle.usecase.GetBattleStatusUseCase
import com.picke.domain.feature.comment.usecase.CommentUseCases
import com.picke.domain.feature.comment.usecase.DeleteCommentUseCase
import com.picke.domain.feature.comment.usecase.LoadCommentsUseCase
import com.picke.domain.feature.comment.usecase.ReportCommentUseCase
import com.picke.domain.feature.comment.usecase.SubmitCommentUseCase
import com.picke.domain.feature.comment.usecase.ToggleCommentLikeUseCase
import com.picke.domain.feature.device.usecase.DeviceUseCases
import com.picke.domain.feature.device.usecase.RegisterDeviceUseCase
import com.picke.domain.feature.explore.usecase.ExploreUseCases
import com.picke.domain.feature.explore.usecase.SearchBattlesUseCase
import com.picke.domain.feature.home.usecase.FetchHomeDataUseCase
import com.picke.domain.feature.home.usecase.HomeUseCases
import com.picke.domain.common.local.CheckNotificationPermissionAskedUseCase
import com.picke.domain.common.local.CheckRefreshToken
import com.picke.domain.common.local.CheckTermsAgreedUseCase
import com.picke.domain.common.local.ClearAllPreferencesUseCase
import com.picke.domain.common.local.GetFcmTokenUseCase
import com.picke.domain.common.local.GetLastAttendanceDateUseCase
import com.picke.domain.common.local.GetLastAttendanceSheetShownDateUseCase
import com.picke.domain.common.local.GetLoginProviderUseCase
import com.picke.domain.common.local.GetUserStatusUseCase
import com.picke.domain.common.local.GetUserTagUseCase
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.common.local.SaveFcmTokenUseCase
import com.picke.domain.common.local.SaveLastAttendanceDateUseCase
import com.picke.domain.common.local.SaveLastAttendanceSheetShownDateUseCase
import com.picke.domain.common.local.SaveLoginProviderUseCase
import com.picke.domain.common.local.SaveNotificationPermissionAskedUseCase
import com.picke.domain.common.local.SaveTermsAgreedUseCase
import com.picke.domain.common.local.SaveUserStatusUseCase
import com.picke.domain.common.local.SaveUserTagUseCase
import com.picke.domain.feature.mypage.usecase.GetCreditHistoryUseCase
import com.picke.domain.feature.mypage.usecase.GetMyBattleRecordsUseCase
import com.picke.domain.feature.mypage.usecase.GetMyContentActivitiesUseCase
import com.picke.domain.feature.mypage.usecase.GetMyPageInfoUseCase
import com.picke.domain.feature.mypage.usecase.GetMyRecapUseCase
import com.picke.domain.feature.mypage.usecase.GetNotificationSettingsUseCase
import com.picke.domain.feature.mypage.usecase.MyPageUseCases
import com.picke.domain.feature.mypage.usecase.UpdateNotificationSettingsUseCase
import com.picke.domain.feature.perspective.usecase.DeletePerspectiveUseCase
import com.picke.domain.feature.perspective.usecase.GetMyPerspectiveUseCase
import com.picke.domain.feature.perspective.usecase.LoadMainPerspectiveUseCase
import com.picke.domain.feature.perspective.usecase.LoadPerspectivesUseCase
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.domain.feature.perspective.usecase.ReportPerspectiveUseCase
import com.picke.domain.feature.perspective.usecase.RetryModerationUseCase
import com.picke.domain.feature.perspective.usecase.SubmitPerspectiveUseCase
import com.picke.domain.feature.perspective.usecase.TogglePerspectiveLikeUseCase
import com.picke.domain.feature.pollquiz.usecase.GetTodayPickVoteUseCase
import com.picke.domain.feature.pollquiz.usecase.PollQuizUseCases
import com.picke.domain.feature.pollquiz.usecase.SubmitTodayPickVoteUseCase
import com.picke.domain.feature.proposal.usecase.ProposalUseCases
import com.picke.domain.feature.proposal.usecase.SubmitProposalUseCase
import com.picke.domain.feature.recommend.usecase.GetInterestingRecommendationsUseCase
import com.picke.domain.feature.recommend.usecase.RecommendUseCases
import com.picke.domain.feature.scenario.usecase.FetchBattleScenarioUseCase
import com.picke.domain.feature.scenario.usecase.ScenarioUseCases
import com.picke.domain.feature.share.usecase.GetBattleShareLinkUseCase
import com.picke.domain.feature.share.usecase.GetRecapDetailUseCase
import com.picke.domain.feature.share.usecase.GetRecapShareKeyUseCase
import com.picke.domain.feature.share.usecase.ShareUseCases
import com.picke.domain.feature.todaybattle.usecase.FetchTodayBattlesUseCase
import com.picke.domain.feature.todaybattle.usecase.TodayBattleUseCases
import com.picke.domain.feature.vote.usecase.GetMyVoteHistoryUseCase
import com.picke.domain.feature.vote.usecase.GetVoteStatsUseCase
import com.picke.domain.feature.vote.usecase.SubmitVoteUseCase
import com.picke.domain.feature.vote.usecase.VoteUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideAlarmUseCases(
        alarmRepository: AlarmRepository
    ): AlarmUseCases {
        return AlarmUseCases(
            getAlarmsUseCase = GetAlarmsUseCase(alarmRepository),
            getAlarmDetailUseCase = GetAlarmDetailUseCase(alarmRepository),
            getUnreadAlarmStatusUseCase = GetUnreadAlarmStatusUseCase(alarmRepository),
            readAlarmUseCase = ReadAlarmUseCase(alarmRepository),
            readAllAlarmsUseCase = ReadAllAlarmsUseCase(alarmRepository)
        )
    }

    @Provides
    fun provideAttendanceUseCases(
        attendanceRepository: AttendanceRepository
    ): AttendanceUseCases {
        return AttendanceUseCases(
            checkAttendanceUseCase = CheckAttendanceUseCase(attendanceRepository),
            getWeeklyAttendanceUseCase = GetWeeklyAttendanceUseCase(attendanceRepository)
        )
    }

    @Provides
    fun provideAuthUseCases(
        authRepository: AuthRepository,
        deviceRepository: DeviceRepository
    ): AuthUseCases {
        return AuthUseCases(
            loginUseCase = LoginUseCase(authRepository),
            logoutUseCase = LogoutUseCase(authRepository, deviceRepository),
            refreshAccessTokenUseCase = RefreshAccessTokenUseCase(authRepository),
            withdrawUseCase = WithdrawUseCase(authRepository)
        )
    }

    @Provides
    fun provideBattleUseCases(
        battleRepository: BattleRepository
    ): BattleUseCases {
        return BattleUseCases(
            getBattleDetailUseCase = GetBattleDetailUseCase(battleRepository),
            getBattleStatusUseCase = GetBattleStatusUseCase(battleRepository)
        )
    }

    @Provides
    fun provideCommentUseCases(
        commentRepository: CommentRepository
    ): CommentUseCases {
        return CommentUseCases(
            loadCommentsUseCase = LoadCommentsUseCase(commentRepository),
            deleteCommentUseCase = DeleteCommentUseCase(commentRepository),
            reportCommentUseCase = ReportCommentUseCase(commentRepository),
            submitCommentUseCase = SubmitCommentUseCase(commentRepository),
            toggleCommentLikeUseCase = ToggleCommentLikeUseCase(commentRepository)
        )
    }

    @Provides
    fun provideDeviceUseCases(
        deviceRepository: DeviceRepository
    ): DeviceUseCases {
        return DeviceUseCases(
            registerDeviceUseCase = RegisterDeviceUseCase(deviceRepository)
        )
    }

    @Provides
    fun provideExploreUseCases(
        exploreRepository: ExploreRepository
    ): ExploreUseCases {
        return ExploreUseCases(
            searchBattlesUseCase = SearchBattlesUseCase(exploreRepository)
        )
    }

    @Provides
    fun provideHomeUseCases(
        homeRepository: HomeRepository
    ): HomeUseCases {
        return HomeUseCases(
            fetchHomeDataUseCase = FetchHomeDataUseCase(homeRepository)
        )
    }

    @Provides
    fun provideMyPageUseCases(
        myPageRepository: MyPageRepository
    ): MyPageUseCases {
        return MyPageUseCases(
            getCreditHistoryUseCase = GetCreditHistoryUseCase(myPageRepository),
            getMyBattleRecordsUseCase = GetMyBattleRecordsUseCase(myPageRepository),
            getMyContentActivitiesUseCase = GetMyContentActivitiesUseCase(myPageRepository),
            getMyPageInfoUseCase = GetMyPageInfoUseCase(myPageRepository),
            getMyRecapUseCase = GetMyRecapUseCase(myPageRepository),
            getNotificationSettingsUseCase = GetNotificationSettingsUseCase(myPageRepository),
            updateNotificationSettingsUseCase = UpdateNotificationSettingsUseCase(myPageRepository)
        )
    }

    @Provides
    fun providePerspectiveUseCases(
        perspectiveRepository: PerspectiveRepository
    ): PerspectiveUseCases {
        return PerspectiveUseCases(
            getMyPerspectiveUseCase = GetMyPerspectiveUseCase(perspectiveRepository),
            deletePerspectiveUseCase = DeletePerspectiveUseCase(perspectiveRepository),
            loadMainPerspectiveUseCase = LoadMainPerspectiveUseCase(perspectiveRepository),
            loadPerspectivesUseCase = LoadPerspectivesUseCase(perspectiveRepository),
            reportPerspectiveUseCase = ReportPerspectiveUseCase(perspectiveRepository),
            retryModerationUseCase = RetryModerationUseCase(perspectiveRepository),
            submitPerspectiveUseCase = SubmitPerspectiveUseCase(perspectiveRepository),
            togglePerspectiveLikeUseCase = TogglePerspectiveLikeUseCase(perspectiveRepository)
        )
    }

    @Provides
    fun providePollQuizUseCases(
        pollQuizRepository: PollQuizRepository
    ): PollQuizUseCases {
        return PollQuizUseCases(
            getTodayPickVoteUseCase = GetTodayPickVoteUseCase(pollQuizRepository),
            submitTodayPickVoteUseCase = SubmitTodayPickVoteUseCase(pollQuizRepository)
        )
    }

    @Provides
    fun provideProposalUseCases(
        proposalRepository: ProposalRepository
    ): ProposalUseCases {
        return ProposalUseCases(
            submitProposalUseCase = SubmitProposalUseCase(proposalRepository)
        )
    }

    @Provides
    fun provideRecommendUseCases(
        recommendRepository: RecommendRepository
    ): RecommendUseCases {
        return RecommendUseCases(
            getInterestingRecommendationsUseCase =
                GetInterestingRecommendationsUseCase(recommendRepository)
        )
    }

    @Provides
    fun provideScenarioUseCases(
        scenarioRepository: ScenarioRepository
    ): ScenarioUseCases {
        return ScenarioUseCases(
            fetchBattleScenarioUseCase = FetchBattleScenarioUseCase(scenarioRepository)
        )
    }

    @Provides
    fun provideShareUseCases(
        shareRepository: ShareRepository
    ): ShareUseCases {
        return ShareUseCases(
            getBattleShareLinkUseCase = GetBattleShareLinkUseCase(shareRepository),
            getRecapShareKeyUseCase = GetRecapShareKeyUseCase(shareRepository),
            getRecapDetailUseCase = GetRecapDetailUseCase(shareRepository)
        )
    }

    @Provides
    fun provideTodayBattleUseCases(
        todayBattleRepository: TodayBattleRepository
    ): TodayBattleUseCases {
        return TodayBattleUseCases(
            fetchTodayBattlesUseCase = FetchTodayBattlesUseCase(todayBattleRepository)
        )
    }

    @Provides
    fun provideVoteUseCases(
        voteRepository: VoteRepository
    ): VoteUseCases {
        return VoteUseCases(
            getMyVoteHistoryUseCase = GetMyVoteHistoryUseCase(voteRepository),
            getVoteStatsUseCase = GetVoteStatsUseCase(voteRepository),
            submitVoteUseCase = SubmitVoteUseCase(voteRepository)
        )
    }

    @Provides
    fun provideLocalPreferencesUseCases(
        repository: LocalPreferencesRepository
    ): LocalPreferencesUseCases {
        return LocalPreferencesUseCases(
            checkRefreshToken = CheckRefreshToken(repository),

            saveUserStatus = SaveUserStatusUseCase(repository),
            getUserStatus = GetUserStatusUseCase(repository),
            saveUserTag = SaveUserTagUseCase(repository),
            getUserTag = GetUserTagUseCase(repository),
            saveLoginProvider = SaveLoginProviderUseCase(repository),
            getLoginProvider = GetLoginProviderUseCase(repository),

            saveFcmToken = SaveFcmTokenUseCase(repository),
            getFcmToken = GetFcmTokenUseCase(repository),
            saveTermsAgreed = SaveTermsAgreedUseCase(repository),
            checkTermsAgreed = CheckTermsAgreedUseCase(repository),
            saveNotificationPermissionAsked = SaveNotificationPermissionAskedUseCase(repository),
            checkNotificationPermissionAsked = CheckNotificationPermissionAskedUseCase(repository),

            saveLastAttendanceDate = SaveLastAttendanceDateUseCase(repository),
            getLastAttendanceDate = GetLastAttendanceDateUseCase(repository),
            saveLastAttendanceSheetShownDate = SaveLastAttendanceSheetShownDateUseCase(repository),
            getLastAttendanceSheetShownDate = GetLastAttendanceSheetShownDateUseCase(repository),

            clearAll = ClearAllPreferencesUseCase(repository)
        )
    }
}