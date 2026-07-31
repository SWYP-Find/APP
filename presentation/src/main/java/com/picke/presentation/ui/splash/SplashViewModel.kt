package com.picke.presentation.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.usecase.auth.AuthUseCases
import com.picke.domain.usecase.local.LocalPreferencesUseCases
import com.picke.presentation.ads.AdMobManager
import com.picke.presentation.analytics.AnalyticsScreen
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.OnboardingStep
import com.picke.presentation.util.AppLifecycleObserver
import com.picke.presentation.util.DeepLinkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiState {
    object Loading : SplashUiState() // 로딩중
    object NavigateToLogin : SplashUiState() // 소셜로그인
    object NavigateToOnboarding : SplashUiState() // 온보딩
    data class NavigateToMain(val needsTermsAgreement: Boolean = false) : SplashUiState() // 메인화면
    data class NavigateToOtherPhilosopher(
        val reportId: String,
        val needsTermsAgreement: Boolean = false
    ) : SplashUiState()

    data class NavigateToBattle(val battleId: String, val needsTermsAgreement: Boolean = false) :
        SplashUiState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val localPreferencesUseCases: LocalPreferencesUseCases,
    private val analyticsTracker: AnalyticsTracker,
    private val adMobManager: AdMobManager,
    private val appLifecycleObserver: AppLifecycleObserver
) : ViewModel() {

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        analyticsTracker.trackScreenView(AnalyticsScreen.SPLASH)
        analyticsTracker.trackOnboardingStep(OnboardingStep.SPLASH)
        checkAutoLogin()
    }

    fun markTermsAgreed() {
        localPreferencesUseCases.saveTermsAgreed()
    }

    fun isNotificationPermissionAsked(): Boolean {
        return localPreferencesUseCases.checkNotificationPermissionAsked()
    }

    fun markNotificationPermissionAsked() {
        localPreferencesUseCases.saveNotificationPermissionAsked()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            val checkRefreshToken = localPreferencesUseCases.checkRefreshToken()
            if (checkRefreshToken) {
                val result = authUseCases.refreshAccessTokenUseCase()

                result.onSuccess {
                    val savedUserTag = localPreferencesUseCases.getUserTag()

                    if (savedUserTag != null) {
                        // 3. 믹스패널 유저 식별 + 로그인 슈퍼 프로퍼티 갱신 (DAU 집계)
                        analyticsTracker.onSessionStart(
                            savedUserTag,
                            localPreferencesUseCases.getLoginProvider()
                        )

                        // 4. 광고 미리 로드 (프리패치) - AdMob 미사용으로 비활성화 (추후 재사용 예정)
                        // adMobManager.loadAd(userId = savedUserTag)
                        // Log.d(TAG, "[AdMob] 광고 프리패치 시작")

                        // 5. 출석 체크 (콜드 스타트 - 갱신된 토큰으로 호출)
                        appLifecycleObserver.checkAttendanceIfNeeded()
                    }

                    val needsTermsAgreement = !localPreferencesUseCases.checkTermsAgreed()
                    val pendingReport = DeepLinkManager.pendingReportId
                    val pendingBattle = DeepLinkManager.pendingBattleId

                    when {
                        pendingReport != null -> {
                            _uiState.value = SplashUiState.NavigateToOtherPhilosopher(
                                pendingReport,
                                needsTermsAgreement
                            )
                        }

                        pendingBattle != null -> {
                            _uiState.value =
                                SplashUiState.NavigateToBattle(pendingBattle, needsTermsAgreement)
                        }

                        else -> {
                            _uiState.value = SplashUiState.NavigateToMain(needsTermsAgreement)
                        }
                    }
                }.onFailure {
                    localPreferencesUseCases.clearAll()
                    _uiState.value = SplashUiState.NavigateToLogin
                }
            } else {
                _uiState.value = SplashUiState.NavigateToOnboarding
            }
        }
    }
}