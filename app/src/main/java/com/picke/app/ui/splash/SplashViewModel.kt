package com.picke.app.ui.splash

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.analytics.AnalyticsScreen
import com.picke.app.analytics.AnalyticsTracker
import com.picke.app.analytics.OnboardingStep
import com.picke.app.data.local.TokenManager
import com.picke.app.domain.usecase.auth.RefreshAccessTokenUseCase
import com.picke.app.di.AdMobManager
import com.picke.app.util.AppLifecycleObserver
import com.picke.app.util.DeepLinkManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class SplashUiState{
    object Loading : SplashUiState() // 로딩중
    object NavigateToLogin : SplashUiState() // 소셜로그인
    object NavigateToOnboarding : SplashUiState() // 온보딩
    data class NavigateToMain(val needsTermsAgreement: Boolean = false) : SplashUiState() // 메인화면
    data class NavigateToOtherPhilosopher(val reportId: String, val needsTermsAgreement: Boolean = false) : SplashUiState()
    data class NavigateToBattle(val battleId: String, val needsTermsAgreement: Boolean = false) : SplashUiState()
}

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val refreshAccessTokenUseCase: RefreshAccessTokenUseCase,
    private val tokenManager: TokenManager,
    private val analyticsTracker: AnalyticsTracker,
    private val adMobManager: AdMobManager,
    private val appLifecycleObserver: AppLifecycleObserver
) : ViewModel() {
    companion object {
        private const val TAG = "SplashViewModel_Picke"
    }

    private val _uiState = MutableStateFlow<SplashUiState>(SplashUiState.Loading)
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    init {
        analyticsTracker.trackScreenView(AnalyticsScreen.SPLASH)
        analyticsTracker.trackOnboardingStep(OnboardingStep.SPLASH)
        checkAutoLogin()
    }

    fun markTermsAgreed() {
        tokenManager.saveTermsAgreed()
    }

    fun isNotificationPermissionAsked(): Boolean = tokenManager.isNotificationPermissionAsked()

    fun markNotificationPermissionAsked() {
        tokenManager.saveNotificationPermissionAsked()
    }

    private fun checkAutoLogin() {
        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 자동 로그인 체크 시작")
            delay(1000)

            val localRefreshToken = tokenManager.getRefreshToken()

            if (localRefreshToken.isNullOrBlank()) {
                Log.i(TAG, "[NAV] 토큰 없음: 신규 유저로 판단 -> 온보딩")
                _uiState.value = SplashUiState.NavigateToOnboarding
            } else {
                Log.d(TAG, "[STATE] 기존 토큰 발견: 서버 확인 절차 진입")
                val result = refreshAccessTokenUseCase(localRefreshToken)

                result.onSuccess {
                    Log.i(TAG, "[NAV] 인증 성공: 메인 화면")

                    val savedUserTag = tokenManager.getUserTag()

                    if (savedUserTag != null) {
                        // 3. 믹스패널 유저 식별 + 로그인 슈퍼 프로퍼티 갱신 (DAU 집계)
                        analyticsTracker.onSessionStart(savedUserTag, tokenManager.getLoginProvider())
                        Log.d(TAG, "[Mixpanel] 유저 식별 완료: $savedUserTag")

                        // 4. 광고 미리 로드 (프리패치)
                        adMobManager.loadAd(userId = savedUserTag)
                        Log.d(TAG, "[AdMob] 광고 프리패치 시작")

                        // 5. 출석 체크 (콜드 스타트 - 갱신된 토큰으로 호출)
                        appLifecycleObserver.checkAttendanceIfNeeded()
                    }

                    val needsTermsAgreement = !tokenManager.isTermsAgreed()
                    val pendingReport = DeepLinkManager.pendingReportId
                    val pendingBattle = DeepLinkManager.pendingBattleId

                    when {
                        pendingReport != null -> {
                            Log.i(TAG, "[NAV] 딥링크 감지 -> 상대방 철학자 리포트 화면으로 이동")
                            _uiState.value = SplashUiState.NavigateToOtherPhilosopher(pendingReport, needsTermsAgreement)
                        }
                        pendingBattle != null -> {
                            Log.i(TAG, "[NAV] 딥링크 감지 -> 배틀 화면으로 이동")
                            _uiState.value = SplashUiState.NavigateToBattle(pendingBattle, needsTermsAgreement)
                        }
                        else -> {
                            Log.i(TAG, "[NAV] 일반 접속 -> 메인 화면으로 이동 (약관 동의 필요: $needsTermsAgreement)")
                            _uiState.value = SplashUiState.NavigateToMain(needsTermsAgreement)
                        }
                    }
                }.onFailure { error ->
                    Log.w(TAG, "[NAV] 인증 실패: 로그인 화면")
                    tokenManager.clearAll()
                    _uiState.value = SplashUiState.NavigateToLogin
                }
            }
        }
    }
}