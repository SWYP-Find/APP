package com.picke.app.ui.my

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.data.local.TokenManager
import com.picke.app.di.AdMobManager
import com.picke.app.domain.model.MyPhilosopher
import com.picke.app.domain.model.MyProfile
import com.picke.app.domain.model.MyTier
import com.picke.app.domain.repository.MyPageRepository
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
    val hasNewNotice: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class MyViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository,
    private val tokenManager: TokenManager,
    val adMobManager: AdMobManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyUiState(isLoading = true))
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    init {
        tokenManager.getUserTag()?.let { adMobManager.loadAd(it) }
    }

    fun fetchMyInfo() {
        // 화면이 다시 보이는 시점에 즉시(동기적으로) 로딩 상태로 전환해야
        // 이전에 로드된 UI가 한 프레임이라도 먼저 그려지는 깜빡임을 막을 수 있다.
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            myPageRepository.getMyPageInfo()
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

    fun readNotice() {
        viewModelScope.launch {
            _uiState.update { it.copy(hasNewNotice = false) }
        }
    }
}