package com.picke.app.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import com.picke.app.BuildConfig
import androidx.security.crypto.MasterKey
import com.picke.app.domain.model.UserStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import java.security.KeyStore
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 앱 내 인증 토큰 및 사용자 상태를 안전하게 보관하는 매니저 클래스
 *
 * [EncryptedSharedPreferences]를 사용하여 모든 데이터를 암호화하여 저장합니다.
 * Android Keystore가 무효화되는 예외 상황에 대한 자체 복구 로직을 포함합니다.
 */
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val TAG = "TokenManager_Picke"
    }

    private var prefs: SharedPreferences? = null

    init {
        initEncryptedPrefs()
    }

    /**
     * EncryptedSharedPreferences 초기화 및 Keystore 깨짐 복구 로직
     *
     * 기기 잠금 방식 변경 등으로 인해 마스터 키가 무효화되면 복호화 에러가 발생할 수 있습니다.
     * 이 경우 기존 암호화된 파일을 강제로 물리적 삭제한 후, 재생성을 시도하여 앱 크래시를 방지합니다.
     */
    private fun initEncryptedPrefs() {
        try {
            // STUDY: MasterKey는 Anroid Keystore 시스템을 이용해, 키를 하드웨어 수준에서 안전하게 보관하는 역할
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

            prefs = EncryptedSharedPreferences.create(
                context,
                "auth_prefs_v2",
                masterKey,
                // STUDY: SIV는 '키' 자체를 암호화할 때, GCM은 '실제 값'을 암호화할 때 사용하는 알고리즘
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            Log.d(TAG, "[LOCAL] EncryptedSharedPreferences 초기화 완료")
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] EncryptedSharedPreferences 초기화 실패, 기존 파일 삭제 후 재시도", e)

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
                Log.e(TAG, "[LOCAL] Keystore 마스터 키 삭제 실패", keyStoreException)
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
                Log.e(TAG, "[LOCAL] EncryptedSharedPreferences 재시도마저 실패함", e2)
            }
        }
    }

    // region 1. Access Token 관리
    /**
     * Access Token을 암호화하여 저장
     *
     * @param token 저장할 Access Token 문자열
     */
    fun saveAccessToken(token: String) {
        prefs?.edit()?.putString("access_token", token)?.apply()
        if (BuildConfig.DEBUG) Log.d(TAG, "[LOCAL] AccessToken 저장 완료: $token")
    }

    /**
     * 저장된 Access Token 반환
     *
     * @return 토큰 문자열 (복호화 실패 시 null)
     */
    fun getAccessToken(): String? {
        return try {
            prefs?.getString("access_token", null)
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] AccessToken 로드 중 에러 발생", e)
            null
        }
    }
    // endregion

    // region 2. Refresh Token 관리
    /**
     * Refresh Token을 암호화하여 저장
     *
     * @param token 저장할 Refresh Token 문자열
     */
    fun saveRefreshToken(token: String) {
        prefs?.edit()?.putString("refresh_token", token)?.apply()
        if (BuildConfig.DEBUG) Log.d(TAG, "[LOCAL] RefreshToken 저장 완료: ${token.take(8)}...")
    }

    /**
     * 저장된 Refresh Token 반환
     *
     * @return 토큰 문자열 (복호화 실패 시 null)
     */
    fun getRefreshToken(): String? {
        return try {
            prefs?.getString("refresh_token", null)
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] RefreshToken 로드 중 에러 발생", e)
            null
        }
    }
    // endregion

    // region 3. User 상태 관리
    /**
     * User 상태 저장
     */
    fun saveUserStatus(status: UserStatus) {
        prefs?.edit()?.putString("user_status", status.name)?.apply()
    }

    /**
     * User 상태 반환
     *
     * @return 에러 시 기본값으로 [UserStatus.NONE] 반환
     */
    fun getUserStatus(): UserStatus {
        val statusString = prefs?.getString("user_status", null)
        return try{
            UserStatus.valueOf(statusString ?: "NONE")
        } catch (e: Exception){
            UserStatus.NONE
        }
    }
    // endregion

    // region 4. User 태그 관리
    /**
     * User 태그 저장
     */
    fun saveUserTag(tag: String) {
        prefs?.edit()?.putString("user_tag", tag)?.apply()
    }

    /**
     * User 태그 반환
     */
    fun getUserTag(): String? {
        return prefs?.getString("user_tag", null)
    }
    // endregion

    // region 4-1. 로그인 프로바이더 관리 (Mixpanel login_provider 슈퍼 프로퍼티 갱신용)
    /**
     * 로그인 프로바이더 저장 (예: "kakao", "google")
     */
    fun saveLoginProvider(provider: String) {
        prefs?.edit()?.putString("login_provider", provider)?.apply()
    }

    /**
     * 로그인 프로바이더 반환
     */
    fun getLoginProvider(): String? {
        return prefs?.getString("login_provider", null)
    }
    // endregion

    // region 5. FCM 토큰 관리
    /**
     * FCM 토큰 저장 (서버에 마지막으로 등록한 토큰 추적용)
     *
     * @param token Firebase에서 발급받은 FCM 토큰
     */
    fun saveFcmToken(token: String) {
        prefs?.edit()?.putString("fcm_token", token)?.apply()
        if (BuildConfig.DEBUG) Log.d(TAG, "[LOCAL] FCM 토큰 저장 완료: ${token.take(8)}...")
    }

    /**
     * 저장된 FCM 토큰 반환
     *
     * @return 토큰 문자열 (없으면 null)
     */
    fun getFcmToken(): String? {
        return try {
            prefs?.getString("fcm_token", null)
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] FCM 토큰 로드 중 에러 발생", e)
            null
        }
    }
    // endregion

    // region 6. 약관 동의 여부 관리
    fun saveTermsAgreed() {
        prefs?.edit()?.putBoolean("terms_agreed", true)?.apply()
    }

    fun isTermsAgreed(): Boolean {
        return try {
            prefs?.getBoolean("terms_agreed", false) ?: false
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] terms_agreed 로드 중 에러 발생", e)
            false
        }
    }
    // endregion

    // region 7. 알림 권한 요청 여부 관리 (계정 단위 - clearAll에 포함)
    fun saveNotificationPermissionAsked() {
        prefs?.edit()?.putBoolean("notification_permission_asked", true)?.apply()
    }

    fun isNotificationPermissionAsked(): Boolean {
        return try {
            prefs?.getBoolean("notification_permission_asked", false) ?: false
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] notification_permission_asked 로드 중 에러 발생", e)
            false
        }
    }
    // endregion

    // region 8. 로컬 데이터 삭제
    /**
     * 모든 로컬 데이터 삭제 (로그아웃 / 회원 탈퇴 시 호출)
     * terms_agreed는 한 번 동의하면 영구 보존 - 여기서 삭제하지 않음
     */
    fun clearAll() {
        prefs?.edit()
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
    // endregion

    // region 9. 마지막 출석 체크 날짜 관리 (자정 기준 최초 진입 판단용)
    /**
     * 출석 체크 성공한 날짜를 ISO-8601(yyyy-MM-dd) 문자열로 저장
     */
    fun saveLastAttendanceDate(date: String) {
        prefs?.edit()?.putString("last_attendance_date", date)?.apply()
    }

    /**
     * 마지막으로 출석 체크에 성공한 날짜 반환
     *
     * @return ISO-8601(yyyy-MM-dd) 문자열 (없으면 null)
     */
    fun getLastAttendanceDate(): String? {
        return try {
            prefs?.getString("last_attendance_date", null)
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] last_attendance_date 로드 중 에러 발생", e)
            null
        }
    }
    // endregion

    // region 10. 출석체크 바텀시트 노출 날짜 관리 (하루 최초 진입 시 1회만 노출)
    /**
     * 출석체크 바텀시트를 보여준 날짜를 ISO-8601(yyyy-MM-dd) 문자열로 저장
     */
    fun saveLastAttendanceSheetShownDate(date: String) {
        prefs?.edit()?.putString("last_attendance_sheet_shown_date", date)?.apply()
    }

    /**
     * 마지막으로 출석체크 바텀시트를 보여준 날짜 반환
     *
     * @return ISO-8601(yyyy-MM-dd) 문자열 (없으면 null)
     */
    fun getLastAttendanceSheetShownDate(): String? {
        return try {
            prefs?.getString("last_attendance_sheet_shown_date", null)
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] last_attendance_sheet_shown_date 로드 중 에러 발생", e)
            null
        }
    }
    // endregion
}