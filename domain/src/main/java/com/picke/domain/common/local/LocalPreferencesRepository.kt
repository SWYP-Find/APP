package com.picke.domain.common.local

interface LocalPreferencesRepository {

    fun checkRefreshToken(): Boolean

    // 2. User
    fun saveUserStatus(status: String)
    fun getUserStatus(): String?
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