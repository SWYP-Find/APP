package com.picke.app.ui.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.picke.app.analytics.AnalyticsTracker
import com.picke.app.data.local.TokenManager
import com.picke.app.domain.usecase.LoginUseCase
import com.picke.app.domain.usecase.RegisterDeviceUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState() // 로딩중
    data class Success(val isNewUser: Boolean, val needsTermsAgreement: Boolean) : LoginUiState() // 로그인 성공
    data class Error(val message: String) : LoginUiState() // 에러 발생
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val tokenManager: TokenManager,
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {
    companion object {
        private const val TAG = "LoginViewModel_Picke"
    }

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState : StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    fun markTermsAgreed() {
        tokenManager.saveTermsAgreed()
    }

    fun handleSocialLoginSuccess(provider: String, authCode: String) {
        // API 중복 요청 방지
        if (_uiState.value is LoginUiState.Loading) {
            Log.d(TAG, "[FLOW] 중복 로그인 요청 차단")
            return
        }

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val result = loginUseCase(provider = provider, authCode = authCode)

            result.onSuccess { authToken ->
                val needsTermsAgreement = authToken.isNewUser || !tokenManager.isTermsAgreed()
                Log.i(TAG, "[NAV] ${provider} 로그인 성공 (신규 유저: ${authToken.isNewUser}, 약관 동의 필요: $needsTermsAgreement)")
                _uiState.value = LoginUiState.Success(
                    isNewUser = authToken.isNewUser,
                    needsTermsAgreement = needsTermsAgreement
                )

                val userTag = authToken.userTag ?: "unknown_user"
                tokenManager.saveUserTag(userTag)
                tokenManager.saveLoginProvider(provider)
                analyticsTracker.onLogin(userTag, provider, authToken.isNewUser)

                FirebaseMessaging.getInstance().token
                    .addOnSuccessListener { fcmToken ->
                        Log.d(TAG, "[FCM] 토큰 발급 완료: ${fcmToken.take(20)}...")
                        tokenManager.saveFcmToken(fcmToken)
                        viewModelScope.launch {
                            registerDeviceUseCase(fcmToken)
                                .onSuccess { Log.d(TAG, "[FCM] 서버 등록 완료") }
                                .onFailure { Log.w(TAG, "[FCM] 서버 등록 실패", it) }
                        }
                    }
                    .addOnFailureListener { Log.w(TAG, "[FCM] 토큰 발급 실패", it) }
            }.onFailure { error ->
                Log.w(TAG, "[FLOW] ${provider} 로그인 실패: ${error.message}")
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}
