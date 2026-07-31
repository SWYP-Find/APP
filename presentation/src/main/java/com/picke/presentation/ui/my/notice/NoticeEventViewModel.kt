package com.picke.presentation.ui.my.notice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.model.AlarmDetailBoard
import com.picke.domain.model.AlarmItemBoard
import com.picke.domain.model.NoticeEventItem
import com.picke.domain.usecase.alarm.AlarmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class NoticeEventUiState(
    val noticeList: List<NoticeEventItem> = emptyList(),
    val eventList: List<NoticeEventItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRead: Boolean = false,
    val initialDetailItem: NoticeEventItem? = null
)

@HiltViewModel
class NoticeEventViewModel @Inject constructor(
    private val alarmUseCases: AlarmUseCases
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeEventUiState())
    val uiState: StateFlow<NoticeEventUiState> = _uiState.asStateFlow()

    init {
        fetchNoticeEvents()
    }

    private fun fetchNoticeEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            Log.d("NoticeEventFlow", "🚀 [목록 API 호출] 공지사항, 이벤트 요청 시작")

            val noticeDeferred =
                async { alarmUseCases.getAlarmsUseCase(category = "NOTICE", page = 0, size = 50) }
            val eventDeferred =
                async { alarmUseCases.getAlarmsUseCase(category = "EVENT", page = 0, size = 50) }

            val noticeResult = noticeDeferred.await()
            val eventResult = eventDeferred.await()

            noticeResult.onSuccess { data ->
                Log.d("NoticeEventFlow", "✅ [목록 성공] 공지사항 수신: ${data.items.size}개")
            }.onFailure { error ->
                Log.e("NoticeEventFlow", "❌ [목록 실패] 공지사항 에러: ${error.message}")
            }

            eventResult.onSuccess { data ->
                Log.d("NoticeEventFlow", "✅ [목록 성공] 이벤트 수신: ${data.items.size}개")
            }.onFailure { error ->
                Log.e("NoticeEventFlow", "❌ [목록 실패] 이벤트 에러: ${error.message}")
            }

            val notices =
                noticeResult.getOrNull()?.items?.map { it.toNoticeEventItem("공지사항") } ?: emptyList()
            val events =
                eventResult.getOrNull()?.items?.map { it.toNoticeEventItem("이벤트") } ?: emptyList()

            _uiState.update {
                it.copy(
                    noticeList = notices,
                    eventList = events,
                    isLoading = false
                )
            }
        }
    }

    fun fetchNoticeEventDetail(notificationId: Long) {
        viewModelScope.launch {
            Log.d("NoticeEventFlow", "🚀 [상세 API 호출] ID($notificationId) 정보 요청 시작!")

            _uiState.update { state ->
                val targetId = notificationId.toString()
                state.copy(
                    noticeList = state.noticeList.map { if (it.id == targetId) it.copy(isRead = true) else it },
                    eventList = state.eventList.map { if (it.id == targetId) it.copy(isRead = true) else it }
                )
            }

            val result = alarmUseCases.getAlarmDetailUseCase(notificationId)

            result.onSuccess { detailData ->
                Log.d("NoticeEventFlow", "✅ [상세 성공] 받아온 데이터: $detailData")
            }.onFailure { error ->
                Log.e("NoticeEventFlow", "❌ [상세 실패] 에러 발생: ${error.message}")
            }
        }
    }

    fun fetchInitialDetail(noticeId: Long) {
        viewModelScope.launch {
            Log.d("NoticeEventFlow", "🚀 [알림 진입] ID($noticeId) 상세 요청 시작")
            val result = alarmUseCases.getAlarmDetailUseCase(noticeId)
            result.onSuccess { detailData ->
                Log.d("NoticeEventFlow", "✅ [알림 진입 성공] $detailData")
                _uiState.update { it.copy(initialDetailItem = detailData.toNoticeEventItem()) }
            }.onFailure { error ->
                Log.e("NoticeEventFlow", "❌ [알림 진입 실패] ${error.message}")
            }
        }
    }

    private fun AlarmItemBoard.toNoticeEventItem(typeName: String): NoticeEventItem {
        val formattedDate = this.createdAt.take(10).replace("-", ".")

        return NoticeEventItem(
            id = this.notificationId.toString(),
            type = typeName,
            title = this.title,
            date = formattedDate,
            content = this.body,
            isRead = this.isRead
        )
    }

    private fun AlarmDetailBoard.toNoticeEventItem(): NoticeEventItem {
        val typeName = when (this.category) {
            "NOTICE" -> "공지사항"
            "EVENT" -> "이벤트"
            "CONTENT" -> "콘텐츠"
            else -> "공지사항"
        }
        return NoticeEventItem(
            id = this.notificationId.toString(),
            type = typeName,
            title = this.title,
            date = this.createdAt.take(10).replace("-", "."),
            content = this.body,
            isRead = true
        )
    }
}