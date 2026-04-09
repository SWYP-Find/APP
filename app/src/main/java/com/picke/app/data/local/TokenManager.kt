package com.picke.app.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.picke.app.domain.model.UserStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

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
            Log.d(TAG, "[LOCAL] EncryptedSharedPreferences 초기화 완료")
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] EncryptedSharedPreferences 초기화 실패, 기존 파일 삭제 후 재시도", e)

            // [비상 조치] 키가 꼬여서 복구 불가 상태이므로, 기존 꼬인 파일을 물리적으로 강제 삭제!
            context.getSharedPreferences("auth_prefs_v2", Context.MODE_PRIVATE).edit().clear().apply()

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
                Log.e(TAG, "[LOCAL] EncryptedSharedPreferences 재시도마저 실패함", e2)
            }
        }
    }

    // Access Token 관리
    fun saveAccessToken(token: String) {
        prefs?.edit()?.putString("access_token", token)?.apply()
        Log.d(TAG, "[LOCAL] AccessToken 저장: ${token}")
    }

    fun getAccessToken(): String? {
        return try {
            prefs?.getString("access_token", null)
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] AccessToken 로드 중 에러 발생", e)
            null
        }
    }

    // Refresh Token 관리
    fun saveRefreshToken(token: String) {
        prefs?.edit()?.putString("refresh_token", token)?.apply()
        Log.d(TAG, "[LOCAL] RefreshToken 저장: ${token}")
    }

    fun getRefreshToken(): String? {
        return try {
            prefs?.getString("refresh_token", null)
        } catch (e: Exception) {
            Log.e(TAG, "[LOCAL] RefreshToken 로드 중 에러 발생", e)
            null
        }
    }

    // 유저 상태 관리
    fun saveUserStatus(status: UserStatus) {
        prefs?.edit()?.putString("user_status", status.name)?.apply()
    }

    fun getUserStatus(): UserStatus {
        val statusString = prefs?.getString("user_status", null)
        return try{
            UserStatus.valueOf(statusString ?: "NONE")
        } catch (e: Exception){
            UserStatus.NONE
        }
    }

    // 토큰 삭제
    fun clearAll() {
        prefs?.edit()
            ?.remove("access_token")
            ?.remove("refresh_token")
            ?.remove("user_status")
            ?.apply()
    }
}