package com.swyp4.team2.ui.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.PhilosopherInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MyUiState(
    val nickname: String = "사색하는 고양이",
    val userHandle: String = "@user_code",
    val credit: Int = 240,
    val userId: String = "user_123",
    val philosopherInfo: PhilosopherInfo? = null,
    val hasNewNotice: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class MyViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MyUiState())
    val uiState: StateFlow<MyUiState> = _uiState.asStateFlow()

    init {
        fetchMyInfo()
    }

    private fun fetchMyInfo() {
        // TODO: 나중에 서버에서 GET /my-info API 호출 로직으로 교체!
    }

    fun refreshPointsAfterAd() {
        viewModelScope.launch {
            // TODO: 실제로는 서버에 내 포인트를 다시 조회하는 API를 찔러서 정확한 값을 가져와야 합니다.
            _uiState.update { currentState ->
                currentState.copy(credit = currentState.credit + 50)
            }
        }
    }

    fun readNotice() {
        viewModelScope.launch {
            // TODO: 나중에 서버에 "알림 읽음 처리해줘!" 라고 API(예: POST /alarms/read)를 찔러야 합니다.
            _uiState.update { currentState ->
                currentState.copy(hasNewNotice = false)
            }
        }
    }
}