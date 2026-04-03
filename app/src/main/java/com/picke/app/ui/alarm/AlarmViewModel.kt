package com.picke.app.ui.alarm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.AlarmItemBoard
import com.picke.app.domain.repository.AlarmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AlarmUiState(
    val alarmList: List<AlarmItemBoard> = emptyList(),
    val selectedCategory: String = "ALL",
    val isLoading: Boolean = false,
    val page: Int = 0,
    val hasNext: Boolean = true,
    val isPagingLoading: Boolean = false
)
@HiltViewModel
class AlarmViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlarmUiState())
    val uiState: StateFlow<AlarmUiState> = _uiState.asStateFlow()

    init {
        fetchAlarms(isRefresh = true)
    }

    // 상단 필터 칩 클릭 시 호출
    fun setCategory(categoryCode: String) {
        if (_uiState.value.selectedCategory == categoryCode) return

        _uiState.update { it.copy(selectedCategory = categoryCode) }
        fetchAlarms(isRefresh = true)
    }

    // 알림 목록 호출 (초기 로드 및 페이징 겸용)
    fun fetchAlarms(isRefresh: Boolean = false) {
        val currentState = _uiState.value

        if (isRefresh) {
            _uiState.update { it.copy(isLoading = true, page = 0, hasNext = true) }
        } else {
            if (currentState.isPagingLoading || !currentState.hasNext) return
            _uiState.update { it.copy(isPagingLoading = true) }
        }

        viewModelScope.launch {
            val targetPage = if (isRefresh) 0 else _uiState.value.page
            val category = _uiState.value.selectedCategory

            Log.d("AlarmFlow", "🚀 [API 호출] category: $category, page: $targetPage")

            val result = alarmRepository.getAlarms(
                category = category,
                page = targetPage,
                size = 20
            )

            result.onSuccess { data ->
                Log.d("AlarmFlow", "✅ [호출 성공] 아이템 개수: ${data.items.size}, 다음 페이지 존재: ${data.hasNext}")
                _uiState.update { state ->
                    state.copy(
                        alarmList = if (isRefresh) data.items else state.alarmList + data.items,
                        page = targetPage + 1,
                        hasNext = data.hasNext,
                        isLoading = false,
                        isPagingLoading = false
                    )
                }
            }.onFailure { exception ->
                Log.e("AlarmFlow", "❌ [호출 실패] 에러 발생!", exception)
                Log.e("AlarmFlow", "실패 메시지: ${exception.message}")
                Log.e("AlarmFlow", "원인(Cause): ${exception.cause}")

                _uiState.update { it.copy(isLoading = false, isPagingLoading = false) }
            }
        }
    }

    // 알림 상세 조회
    fun fetchAlarmDetail(notificationId: Long) {
        viewModelScope.launch {
            Log.d("AlarmFlow", "▶️ 개별 알림 상세 정보 요청 시작! (ID: $notificationId)")
            val result = alarmRepository.getAlarmDetail(notificationId)

            result.onSuccess { detailData ->
                Log.d("AlarmFlow", "✅ 알림 상세 조회 성공: $detailData")
                // TODO: 받아온 detailData를 활용해 팝업을 띄우거나 화면을 이동하는 상태 추가
            }.onFailure { error ->
                Log.e("AlarmFlow", "❌ 알림 상세 조회 실패: ${error.message}")
            }
        }
    }

    // 개별 알림 읽음 처리
    fun readAlarm(notificationId: Long) {
        viewModelScope.launch {
            Log.d("AlarmFlow", "▶️ 개별 알림 읽음 처리 API 호출 시작! (notificationId: $notificationId)")
            val result = alarmRepository.readAlarm(notificationId)

            result.onSuccess {
                Log.d("AlarmFlow", "✅ 개별 알림 읽음 처리 성공!")
                _uiState.update { state ->
                    state.copy(
                        alarmList = state.alarmList.map { item ->
                            if (item.notificationId == notificationId) item.copy(isRead = true)
                            else item
                        }
                    )
                }
            }.onFailure { error ->
                Log.e("AlarmFlow", "❌ 개별 알림 읽음 처리 실패...")
            }
        }
    }

    // 모두 읽음 처리
    fun readAllAlarms() {
        viewModelScope.launch {
            Log.d("AlarmFlow", "▶️ 전체 알림 읽음 처리 API 호출 시작!")
            val result = alarmRepository.readAllAlarms()

            result.onSuccess {
                Log.d("AlarmFlow", "✅ 전체 알림 읽음 처리 성공!")
                _uiState.update { state ->
                    state.copy(
                        alarmList = state.alarmList.map { it.copy(isRead = true) }
                    )
                }
            }.onFailure { error ->
                Log.e("AlarmFlow", "❌ 전체 알림 읽음 처리 실패...")
            }
        }
    }
}