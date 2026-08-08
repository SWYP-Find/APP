package com.picke.presentation.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.messaging.FirebaseMessaging
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.auth.usecase.AuthUseCases
import com.picke.domain.feature.device.usecase.DeviceUseCases
import com.picke.presentation.BuildConfig
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.ui.login.model.LoginUiState
import com.picke.presentation.ui.login.model.Provider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val deviceUseCases: DeviceUseCases,
    private val preferencesUseCases: LocalPreferencesUseCases,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun resetState() {
        _uiState.value = LoginUiState.Idle
    }

    fun markTermsAgreed() {
        preferencesUseCases.saveTermsAgreed()
    }

    fun handleSocialLoginSuccess(provider: Provider, authCode: String) {
        if (_uiState.value is LoginUiState.Loading) return

        _uiState.value = LoginUiState.Loading

        viewModelScope.launch {
            val redirectUri = when (provider) {
                Provider.KAKAO -> "kakao${BuildConfig.KAKAO_DEBUG_APPKEY}://oauth"
                Provider.GOOGLE -> "https://picke.store/oauth/google"
            }

            val result = authUseCases.loginUseCase(
                redirectUri = redirectUri,
                provider = provider.name,
                authCode = authCode
            )

            result.onSuccess { authToken ->
                val needsTermsAgreement =
                    authToken.isNewUser || !preferencesUseCases.checkTermsAgreed()

                _uiState.value = LoginUiState.Success(
                    isNewUser = authToken.isNewUser,
                    needsTermsAgreement = needsTermsAgreement
                )

                val userTag = authToken.userTag ?: "unknown_user"
                preferencesUseCases.saveUserTag(userTag)
                preferencesUseCases.saveLoginProvider(provider.name)
                analyticsTracker.onLogin(userTag, provider.name, authToken.isNewUser)

                FirebaseMessaging.getInstance().token
                    .addOnSuccessListener { fcmToken ->
                        preferencesUseCases.saveFcmToken(fcmToken)
                        viewModelScope.launch {
                            deviceUseCases.registerDeviceUseCase(fcmToken)
                        }
                    }
            }.onFailure { error ->
                _uiState.value = LoginUiState.Error(error.message ?: "로그인에 실패했습니다.")
            }
        }
    }
}