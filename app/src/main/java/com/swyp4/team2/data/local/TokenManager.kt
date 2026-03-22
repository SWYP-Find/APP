package com.swyp4.team2.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var prefs: SharedPreferences? = null

    init {
        initEncryptedPrefs()
    }

    private fun initEncryptedPrefs() {
        try {
            Log.d("TokenManager", "1. 암호화 저장소(MasterKey) 생성 시도...")
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
            Log.d("TokenManager", "2. 🟢 암호화 저장소 초기화 성공!")

        } catch (e: Exception) {
            Log.e("TokenManager", "2. 🔴 암호화 저장소 초기화 실패! (키스토어 꼬임 발생)", e)

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
                Log.d("TokenManager", "3. 🟡 기존 파일 삭제 후 암호화 저장소 재성성 완료 (유저는 재로그인 필요)")
            } catch (e2: Exception) {
                Log.e("TokenManager", "3. 🔴 재생성조차 실패함 (심각한 디바이스 에러)", e2)
            }
        }
    }

    // Access Token 관리
    fun saveAccessToken(token: String) {
        prefs?.edit()?.putString("access_token", token)?.apply()
    }
    fun getAccessToken(): String? {
        return try {
            prefs?.getString("access_token", null)
        } catch (e: Exception) {
            Log.e("TokenManager", "AccessToken 읽기 실패 (복호화 에러)", e)
            null
        }
    }

    // Refresh Token 관리
    fun saveRefreshToken(token: String) {
        prefs?.edit()?.putString("refresh_token", token)?.apply()
    }
    fun getRefreshToken(): String? {
        return try {
            prefs?.getString("refresh_token", null)
        } catch (e: Exception) {
            Log.e("TokenManager", "RefreshToken 읽기 실패 (복호화 에러)", e)
            null
        }
    }

    // 유저 상태(PENDING/ACTIVE) 관리
    fun saveUserStatus(status: String) {
        prefs?.edit()?.putString("user_status", status)?.apply()
    }
    fun getUserStatus(): String? {
        return try {
            prefs?.getString("user_status", null)
        } catch (e: Exception) {
            null
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