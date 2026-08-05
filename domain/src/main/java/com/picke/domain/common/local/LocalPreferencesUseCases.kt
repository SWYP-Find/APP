package com.picke.domain.common.local

data class LocalPreferencesUseCases(
    val checkRefreshToken: CheckRefreshToken,

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