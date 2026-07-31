package com.picke.domain.repository

import com.picke.domain.model.UserStatus

interface LocalPreferencesRepository {
    // 1. Token
    fun saveAccessToken(token: String)
    fun getAccessToken(): String?
    fun saveRefreshToken(token: String)
    fun getRefreshToken(): String?

    // 2. User
    fun saveUserStatus(status: UserStatus)
    fun getUserStatus(): UserStatus // String 대신 Enum으로 반환
    fun saveUserTag(tag: String)
    fun getUserTag(): String?
    fun saveLoginProvider(provider: String)
    fun getLoginProvider(): String?

    // 3. Settings & FCM
    fun saveFcmToken(token: String)
    fun getFcmToken(): String?
    fun saveTermsAgreed()
    fun isTermsAgreed(): Boolean
    fun saveNotificationPermissionAsked()
    fun isNotificationPermissionAsked(): Boolean

    // 4. Attendance
    fun saveLastAttendanceDate(date: String)
    fun getLastAttendanceDate(): String?
    fun saveLastAttendanceSheetShownDate(date: String)
    fun getLastAttendanceSheetShownDate(): String?

    // 5. Clear
    fun clearAll()
}