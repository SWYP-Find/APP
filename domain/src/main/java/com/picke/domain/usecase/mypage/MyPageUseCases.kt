package com.picke.domain.usecase.mypage

data class MyPageUseCases(
    val getCreditHistoryUseCase: GetCreditHistoryUseCase,
    val getMyBattleRecordsUseCase: GetMyBattleRecordsUseCase,
    val getMyContentActivitiesUseCase: GetMyContentActivitiesUseCase,
    val getMyPageInfoUseCase: GetMyPageInfoUseCase,
    val getMyRecapUseCase: GetMyRecapUseCase,
    val getNotificationSettingsUseCase: GetNotificationSettingsUseCase,
    val updateNotificationSettingsUseCase: UpdateNotificationSettingsUseCase
)