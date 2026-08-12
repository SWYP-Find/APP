package com.picke.presentation.ui.my.user

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.mypage.model.MyPhilosopher
import com.picke.domain.feature.mypage.model.MyProfile
import com.picke.domain.feature.mypage.model.MyTier
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.alarm.usecase.AlarmUseCases
import com.picke.domain.feature.mypage.usecase.MyPageUseCases
import com.picke.presentation.ads.AdMobManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyUiState(
    val profile: MyProfile? = null,
    val philosopher: MyPhilosopher? = null,
    val tier: MyTier? = null,
    val hasNewNotice: Boolean = false,
    val isLoading: Boolean = false,
    // 미읽음 알림 여부 조회가 끝나기 전까지 탑바 아이콘을 shimmer로 보여주기 위한 플래그
    val isAlarmStatusLoading: Boolean = true
)

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myPageUseCases: MyPageUseCases,
    private val alarmUseCases: AlarmUseCases,
    private val localPreferencesUseCases: LocalPreferencesUseCases,
    val adMobManager: AdMobManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyUiState(isLoading = true))
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    init {
        localPreferencesUseCases.getUserTag()?.let { adMobManager.loadAd(it) }
    }

    fun fetchMyInfo() {
        // 화면이 다시 보이는 시점에 즉시(동기적으로) 로딩 상태로 전환해야
        // 이전에 로드된 UI가 한 프레임이라도 먼저 그려지는 깜빡임을 막을 수 있다.
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            myPageUseCases.getMyPageInfoUseCase()
                .onSuccess { infoBoard ->
                    Log.d("MyPageFlow", "🟢 마이페이지 정보 로드 성공: ${infoBoard}")
                    _uiState.update {
                        it.copy(
                            profile = infoBoard.profile,
                            philosopher = infoBoard.philosopher,
                            tier = infoBoard.tier,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("MyPageFlow", "🔴 마이페이지 로드 실패: ${error.message}", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun refreshPointsAfterAd() {
        _uiState.update { currentState ->
            val currentTier = currentState.tier
            if (currentTier != null) {
                currentState.copy(
                    tier = currentTier.copy(currentPoint = currentTier.currentPoint + 20)
                )
            } else {
                currentState
            }
        }
        fetchMyInfo()
    }

    // 미읽음 알림 존재 여부 조회 (탑바 벨 아이콘 빨간 점 배지)
    fun fetchUnreadAlarmStatus() {
        // 재진입 시에도 배지 여부가 확정되기 전까지 아이콘을 shimmer로 유지한다.
        _uiState.update { it.copy(isAlarmStatusLoading = true) }

        viewModelScope.launch {
            alarmUseCases.getUnreadAlarmStatusUseCase()
                .onSuccess { hasUnread ->
                    _uiState.update {
                        it.copy(
                            hasNewNotice = hasUnread,
                            isAlarmStatusLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("MyPageFlow", "🔴 미읽음 알림 여부 조회 실패!", error)
                    _uiState.update { it.copy(isAlarmStatusLoading = false) }
                }
        }
    }

}