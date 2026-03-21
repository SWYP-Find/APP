package com.swyp4.team2.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

// 서버 통신용 인증 토큰을 기기에 저장하고 관리하는 클래스
@Singleton
class TokenManager @Inject constructor(
    @ApplicationContext context: Context
) {
    // 1. Android KeyStore에 저장될 MasterKey 생성
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    // 2. Token을 저장할 암호화된 SharedPreferences 생성
    private val prefs = EncryptedSharedPreferences.create(
        context,
        "auth_prefs_v2",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    // Access Token 관리
    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }
    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    // Refresh Token 관리
    fun saveRefreshToken(token: String) {
        prefs.edit().putString("refresh_token", token).apply()
    }
    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    // 유저 상태(PENDING/ACTIVE) 관리
    fun saveUserStatus(status: String) {
        prefs.edit().putString("user_status", status).apply()
    }
    fun getUserStatus(): String? {
        return prefs.getString("user_status", null)
    }

    // 토큰 삭제 (로그아웃, 회원탈퇴, 인증 만료 시 호출)
    fun clearAll() {
        prefs.edit()
            .remove("access_token")
            .remove("refresh_token")
            .remove("user_status")
            .apply()
    }
}