package com.picke.data.common.local

import com.picke.domain.common.local.LocalPreferencesRepository
import javax.inject.Inject

class LocalPreferencesRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager
) : LocalPreferencesRepository {

    override fun checkRefreshToken() = preferencesManager.getRefreshToken()?.isNotEmpty() ?: false

    override fun saveUserStatus(status: String) = preferencesManager.saveUserStatus(status)
    override fun getUserStatus() = preferencesManager.getUserStatus()

    override fun saveUserTag(tag: String) = preferencesManager.saveUserTag(tag)
    override fun getUserTag() = preferencesManager.getUserTag()

    override fun saveLoginProvider(provider: String) = preferencesManager.saveLoginProvider(provider)
    override fun getLoginProvider() = preferencesManager.getLoginProvider()

    override fun saveFcmToken(token: String) = preferencesManager.saveFcmToken(token)
    override fun getFcmToken() = preferencesManager.getFcmToken()

    override fun saveTermsAgreed() = preferencesManager.saveTermsAgreed()
    override fun isTermsAgreed() = preferencesManager.isTermsAgreed()

    override fun saveNotificationPermissionAsked() = preferencesManager.saveNotificationPermissionAsked()
    override fun isNotificationPermissionAsked() = preferencesManager.isNotificationPermissionAsked()

    override fun saveLastAttendanceDate(date: String) = preferencesManager.saveLastAttendanceDate(date)
    override fun getLastAttendanceDate() = preferencesManager.getLastAttendanceDate()

    override fun saveLastAttendanceSheetShownDate(date: String) = preferencesManager.saveLastAttendanceSheetShownDate(date)
    override fun getLastAttendanceSheetShownDate() = preferencesManager.getLastAttendanceSheetShownDate()

    override fun clearAll() = preferencesManager.clearAll()
}