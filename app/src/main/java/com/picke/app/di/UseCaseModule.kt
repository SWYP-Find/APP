package com.picke.app.di

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
import com.picke.domain.usecase.local.CheckNotificationPermissionAskedUseCase
import com.picke.domain.usecase.local.CheckTermsAgreedUseCase
import com.picke.domain.usecase.local.ClearAllPreferencesUseCase
import com.picke.domain.usecase.local.GetAccessTokenUseCase
import com.picke.domain.usecase.local.GetFcmTokenUseCase
import com.picke.domain.usecase.local.GetLastAttendanceDateUseCase
import com.picke.domain.usecase.local.GetLastAttendanceSheetShownDateUseCase
import com.picke.domain.usecase.local.GetLoginProviderUseCase
import com.picke.domain.usecase.local.GetRefreshTokenUseCase
import com.picke.domain.usecase.local.GetUserStatusUseCase
import com.picke.domain.usecase.local.GetUserTagUseCase
import com.picke.domain.usecase.local.LocalPreferencesUseCases
import com.picke.domain.usecase.local.SaveAccessTokenUseCase
import com.picke.domain.usecase.local.SaveFcmTokenUseCase
import com.picke.domain.usecase.local.SaveLastAttendanceDateUseCase
import com.picke.domain.usecase.local.SaveLastAttendanceSheetShownDateUseCase
import com.picke.domain.usecase.local.SaveLoginProviderUseCase
import com.picke.domain.usecase.local.SaveNotificationPermissionAskedUseCase
import com.picke.domain.usecase.local.SaveRefreshTokenUseCase
import com.picke.domain.usecase.local.SaveTermsAgreedUseCase
import com.picke.domain.usecase.local.SaveUserStatusUseCase
import com.picke.domain.usecase.local.SaveUserTagUseCase
import com.picke.domain.usecase.alarm.AlarmUseCases
import com.picke.domain.usecase.alarm.GetAlarmDetailUseCase
import com.picke.domain.usecase.alarm.GetAlarmsUseCase
import com.picke.domain.usecase.alarm.GetUnreadAlarmStatusUseCase
import com.picke.domain.usecase.alarm.ReadAlarmUseCase
import com.picke.domain.usecase.alarm.ReadAllAlarmsUseCase
import com.picke.domain.usecase.attendance.AttendanceUseCases
import com.picke.domain.usecase.attendance.CheckAttendanceUseCase
import com.picke.domain.usecase.attendance.GetWeeklyAttendanceUseCase
import com.picke.domain.usecase.auth.AuthUseCases
import com.picke.domain.usecase.auth.LoginUseCase
import com.picke.domain.usecase.auth.LogoutUseCase
import com.picke.domain.usecase.auth.RefreshAccessTokenUseCase
import com.picke.domain.usecase.auth.WithdrawUseCase
import com.picke.domain.usecase.battle.BattleUseCases
import com.picke.domain.usecase.battle.GetBattleDetailUseCase
import com.picke.domain.usecase.battle.GetBattleStatusUseCase
import com.picke.domain.usecase.comment.CommentUseCases
import com.picke.domain.usecase.comment.DeleteCommentUseCase
import com.picke.domain.usecase.comment.LoadCommentsUseCase
import com.picke.domain.usecase.comment.ReportCommentUseCase
import com.picke.domain.usecase.comment.SubmitCommentUseCase
import com.picke.domain.usecase.comment.ToggleCommentLikeUseCase
import com.picke.domain.usecase.device.DeviceUseCases
import com.picke.domain.usecase.device.RegisterDeviceUseCase
import com.picke.domain.usecase.explore.ExploreUseCases
import com.picke.domain.usecase.explore.SearchBattlesUseCase
import com.picke.domain.usecase.home.FetchHomeDataUseCase
import com.picke.domain.usecase.home.HomeUseCases
import com.picke.domain.usecase.mypage.GetCreditHistoryUseCase
import com.picke.domain.usecase.mypage.GetMyBattleRecordsUseCase
import com.picke.domain.usecase.mypage.GetMyContentActivitiesUseCase
import com.picke.domain.usecase.mypage.GetMyPageInfoUseCase
import com.picke.domain.usecase.mypage.GetMyRecapUseCase
import com.picke.domain.usecase.mypage.GetNotificationSettingsUseCase
import com.picke.domain.usecase.mypage.MyPageUseCases
import com.picke.domain.usecase.mypage.UpdateNotificationSettingsUseCase
import com.picke.domain.usecase.perspective.DeletePerspectiveUseCase
import com.picke.domain.usecase.perspective.GetMyPerspectiveUseCase
import com.picke.domain.usecase.perspective.LoadMainPerspectiveUseCase
import com.picke.domain.usecase.perspective.LoadPerspectivesUseCase
import com.picke.domain.usecase.perspective.PerspectiveUseCases
import com.picke.domain.usecase.perspective.ReportPerspectiveUseCase
import com.picke.domain.usecase.perspective.RetryModerationUseCase
import com.picke.domain.usecase.perspective.SubmitPerspectiveUseCase
import com.picke.domain.usecase.perspective.TogglePerspectiveLikeUseCase
import com.picke.domain.usecase.pollquiz.GetTodayPickVoteUseCase
import com.picke.domain.usecase.pollquiz.PollQuizUseCases
import com.picke.domain.usecase.pollquiz.SubmitTodayPickVoteUseCase
import com.picke.domain.usecase.proposal.ProposalUseCases
import com.picke.domain.usecase.proposal.SubmitProposalUseCase
import com.picke.domain.usecase.recommend.GetInterestingRecommendationsUseCase
import com.picke.domain.usecase.recommend.RecommendUseCases
import com.picke.domain.usecase.scenario.FetchBattleScenarioUseCase
import com.picke.domain.usecase.scenario.ScenarioUseCases
import com.picke.domain.usecase.share.GetBattleShareLinkUseCase
import com.picke.domain.usecase.share.GetRecapDetailUseCase
import com.picke.domain.usecase.share.GetRecapShareKeyUseCase
import com.picke.domain.usecase.share.ShareUseCases
import com.picke.domain.usecase.todaybattle.FetchTodayBattlesUseCase
import com.picke.domain.usecase.todaybattle.TodayBattleUseCases
import com.picke.domain.usecase.vote.GetMyVoteHistoryUseCase
import com.picke.domain.usecase.vote.GetVoteStatsUseCase
import com.picke.domain.usecase.vote.SubmitVoteUseCase
import com.picke.domain.usecase.vote.VoteUseCases
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
            saveAccessToken = SaveAccessTokenUseCase(repository),
            getAccessToken = GetAccessTokenUseCase(repository),
            saveRefreshToken = SaveRefreshTokenUseCase(repository),
            getRefreshToken = GetRefreshTokenUseCase(repository),

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