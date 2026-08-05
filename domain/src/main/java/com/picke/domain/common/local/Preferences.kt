package com.picke.domain.common.local

class CheckRefreshToken(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): Boolean = repo.checkRefreshToken()
}

class SaveUserStatusUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(status: String) = repo.saveUserStatus(status)
}

class GetUserStatusUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): String? = repo.getUserStatus()
}

class SaveUserTagUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(tag: String) = repo.saveUserTag(tag)
}

class GetUserTagUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): String? = repo.getUserTag()
}

class SaveLoginProviderUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(provider: String) = repo.saveLoginProvider(provider)
}

class GetLoginProviderUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): String? = repo.getLoginProvider()
}

class SaveFcmTokenUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(token: String) = repo.saveFcmToken(token)
}

class GetFcmTokenUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): String? = repo.getFcmToken()
}

class SaveTermsAgreedUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke() = repo.saveTermsAgreed()
}

class CheckTermsAgreedUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): Boolean = repo.isTermsAgreed()
}

class SaveNotificationPermissionAskedUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke() = repo.saveNotificationPermissionAsked()
}

class CheckNotificationPermissionAskedUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): Boolean = repo.isNotificationPermissionAsked()
}

class SaveLastAttendanceDateUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(date: String) = repo.saveLastAttendanceDate(date)
}

class GetLastAttendanceDateUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): String? = repo.getLastAttendanceDate()
}

class SaveLastAttendanceSheetShownDateUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(date: String) = repo.saveLastAttendanceSheetShownDate(date)
}

class GetLastAttendanceSheetShownDateUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke(): String? = repo.getLastAttendanceSheetShownDate()
}

class ClearAllPreferencesUseCase(private val repo: LocalPreferencesRepository) {
    operator fun invoke() = repo.clearAll()
}