package com.picke.domain.usecase.local

data class LocalPreferencesUseCases(
    val saveAccessToken: SaveAccessTokenUseCase,
    val getAccessToken: GetAccessTokenUseCase,
    val saveRefreshToken: SaveRefreshTokenUseCase,
    val getRefreshToken: GetRefreshTokenUseCase,

    val saveUserStatus: SaveUserStatusUseCase,
    val getUserStatus: GetUserStatusUseCase,
    val saveUserTag: SaveUserTagUseCase,
    val getUserTag: GetUserTagUseCase,
    val saveLoginProvider: SaveLoginProviderUseCase,
    val getLoginProvider: GetLoginProviderUseCase,

    val saveFcmToken: SaveFcmTokenUseCase,
    val getFcmToken: GetFcmTokenUseCase,
    val saveTermsAgreed: SaveTermsAgreedUseCase,
    val checkTermsAgreed: CheckTermsAgreedUseCase,
    val saveNotificationPermissionAsked: SaveNotificationPermissionAskedUseCase,
    val checkNotificationPermissionAsked: CheckNotificationPermissionAskedUseCase,

    val saveLastAttendanceDate: SaveLastAttendanceDateUseCase,
    val getLastAttendanceDate: GetLastAttendanceDateUseCase,
    val saveLastAttendanceSheetShownDate: SaveLastAttendanceSheetShownDateUseCase,
    val getLastAttendanceSheetShownDate: GetLastAttendanceSheetShownDateUseCase,

    val clearAll: ClearAllPreferencesUseCase
)