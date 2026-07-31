package com.picke.data.repository

import com.picke.data.local.PreferencesManager
import com.picke.domain.model.UserStatus
import com.picke.domain.repository.LocalPreferencesRepository
import javax.inject.Inject

class LocalPreferencesRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : LocalPreferencesRepository {

    override fun saveAccessToken(token: String) = preferencesManager.saveAccessToken(token)
    override fun getAccessToken(): String? = preferencesManager.getAccessToken()

    override fun saveRefreshToken(token: String) = preferencesManager.saveRefreshToken(token)
    override fun getRefreshToken(): String? = preferencesManager.getRefreshToken()

    override fun saveUserStatus(status: UserStatus) {
        preferencesManager.saveUserStatus(status)
    }

    override fun getUserStatus(): UserStatus {
        val statusString = preferencesManager.getUserStatus()
        return try {
            if (statusString != null) UserStatus.valueOf(statusString) else UserStatus.NONE
        } catch (e: Exception) {
            UserStatus.NONE // 파싱 에러나 값이 없을 경우 기본값
        }
    }

    override fun saveUserTag(tag: String) = preferencesManager.saveUserTag(tag)
    override fun getUserTag(): String? = preferencesManager.getUserTag()

    override fun saveLoginProvider(provider: String) = preferencesManager.saveLoginProvider(provider)
    override fun getLoginProvider(): String? = preferencesManager.getLoginProvider()

    override fun saveFcmToken(token: String) = preferencesManager.saveFcmToken(token)
    override fun getFcmToken(): String? = preferencesManager.getFcmToken()

    override fun saveTermsAgreed() = preferencesManager.saveTermsAgreed()
    override fun isTermsAgreed(): Boolean = preferencesManager.isTermsAgreed()

    override fun saveNotificationPermissionAsked() = preferencesManager.saveNotificationPermissionAsked()
    override fun isNotificationPermissionAsked(): Boolean = preferencesManager.isNotificationPermissionAsked()

    override fun saveLastAttendanceDate(date: String) = preferencesManager.saveLastAttendanceDate(date)
    override fun getLastAttendanceDate(): String? = preferencesManager.getLastAttendanceDate()

    override fun saveLastAttendanceSheetShownDate(date: String) = preferencesManager.saveLastAttendanceSheetShownDate(date)
    override fun getLastAttendanceSheetShownDate(): String? = preferencesManager.getLastAttendanceSheetShownDate()

    override fun clearAll() = preferencesManager.clearAll()
}