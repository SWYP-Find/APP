package com.picke.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private lateinit var prefs: SharedPreferences

    init {
        initEncryptedPrefs()
    }

    private fun initEncryptedPrefs() {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            prefs = EncryptedSharedPreferences.create(
                context,
                "auth_prefs_v2",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // NOTE: 유저가 기기의 잠금 방식(PIN, 생체인식 등)을 변경하면 기존 Keystore가 초기화되며 복호화 불가 상태가 됨.
            // clear()는 XML의 keyset 메타데이터를 남기므로 deleteSharedPreferences()로 파일 자체를 삭제해야 함.
            context.deleteSharedPreferences("auth_prefs_v2")
            try {
                val keyStore = KeyStore.getInstance("AndroidKeyStore")
                keyStore.load(null)
                if (keyStore.containsAlias(MasterKey.DEFAULT_MASTER_KEY_ALIAS)) {
                    keyStore.deleteEntry(MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                }
            } catch (keyStoreException: Exception) {
                //
            }

            // 삭제 후 다시 한 번 생성 시도
            try {
                val masterKey = MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build()
                prefs = EncryptedSharedPreferences.create(
                    context,
                    "auth_prefs_v2",
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                )
            } catch (e2: Exception) {
                // FIXME: 재시도마저 실패할 경우, 강제로 로그아웃 처리하거나 유저에게 '앱 재설치 권장' 팝업을 띄우는 예외 처리 로직 추가 고민 필요
            }
        }
    }

    fun saveAccessToken(token: String) {
        prefs.edit()?.putString("access_token", token)?.apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    fun saveRefreshToken(token: String) {
        prefs.edit()?.putString("refresh_token", token)?.apply()
    }

    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    fun saveUserStatus(status: String) {
        prefs.edit()?.putString("user_status", status)?.apply()
    }

    fun getUserStatus(): String? {
        return prefs.getString("user_status", null)
    }

    fun saveUserTag(tag: String) {
        prefs.edit()?.putString("user_tag", tag)?.apply()
    }

    fun getUserTag(): String? {
        return prefs.getString("user_tag", null)
    }

    fun saveLoginProvider(provider: String) {
        prefs.edit()?.putString("login_provider", provider)?.apply()
    }

    fun getLoginProvider(): String? {
        return prefs.getString("login_provider", null)
    }

    fun saveFcmToken(token: String) {
        prefs.edit()?.putString("fcm_token", token)?.apply()
    }

    fun getFcmToken(): String? {
        return prefs.getString("fcm_token", null)
    }

    fun saveTermsAgreed() {
        prefs.edit()?.putBoolean("terms_agreed", true)?.apply()
    }

    fun isTermsAgreed(): Boolean {
        return prefs.getBoolean("terms_agreed", false) ?: false
    }

    fun saveNotificationPermissionAsked() {
        prefs.edit()?.putBoolean("notification_permission_asked", true)?.apply()
    }

    fun isNotificationPermissionAsked(): Boolean {
        return prefs.getBoolean("notification_permission_asked", false) ?: false
    }

    fun clearAll() {
        prefs.edit()
            ?.remove("access_token")
            ?.remove("refresh_token")
            ?.remove("user_status")
            ?.remove("user_tag")
            ?.remove("login_provider")
            ?.remove("fcm_token")
            ?.remove("notification_permission_asked")
            ?.remove("last_attendance_date")
            ?.remove("last_attendance_sheet_shown_date")
            ?.apply()
    }

    fun saveLastAttendanceDate(date: String) {
        prefs.edit()?.putString("last_attendance_date", date)?.apply()
    }

    fun getLastAttendanceDate(): String? {
        return prefs.getString("last_attendance_date", null)
    }

    fun saveLastAttendanceSheetShownDate(date: String) {
        prefs.edit()?.putString("last_attendance_sheet_shown_date", date)?.apply()
    }

    fun getLastAttendanceSheetShownDate(): String? {
        return prefs.getString("last_attendance_sheet_shown_date", null)
    }
}