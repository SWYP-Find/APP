package com.example.swypapp.data.local

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
        "auth_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM,
    )

    // 3. Access Token 관리
    fun saveAccessToken(token: String) {
        prefs.edit().putString("access_token", token).apply()
    }
    fun getAccessToken(): String? {
        return prefs.getString("access_token", null)
    }

    // 4. Refresh Token 관리
    fun saveRefreshToken(token: String) {
        prefs.edit().putString("refresh_token", token).apply()
    }
    fun getRefreshToken(): String? {
        return prefs.getString("refresh_token", null)
    }

    // 5. 토큰 삭제 (로그아웃, 회원탈퇴, 인증 만료 시 호출)
    fun deleteToken() {
        prefs.edit()
            .remove("access_token")
            .remove("refresh_token")
            .apply()
    }
}