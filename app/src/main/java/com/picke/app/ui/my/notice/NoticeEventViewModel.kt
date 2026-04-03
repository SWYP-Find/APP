package com.picke.app.ui.my.notice

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.AlarmItemBoard
import com.picke.app.domain.model.NoticeEventItem
import com.picke.app.domain.repository.AlarmRepository
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
    val contentList: List<NoticeEventItem> = emptyList(),
    val isLoading: Boolean = false,
    val isRead: Boolean = false
)

@HiltViewModel
class NoticeEventViewModel @Inject constructor(
    private val alarmRepository: AlarmRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(NoticeEventUiState())
    val uiState: StateFlow<NoticeEventUiState> = _uiState.asStateFlow()

    init {
        fetchNoticeEvents()
    }

    private fun fetchNoticeEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            Log.d("NoticeEventFlow", "🚀 [목록 API 호출] 공지사항, 이벤트, 콘텐츠 요청 시작")

            val noticeDeferred = async { alarmRepository.getAlarms(category = "NOTICE", page = 0, size = 50) }
            val eventDeferred = async { alarmRepository.getAlarms(category = "EVENT", page = 0, size = 50) }
            val contentDeferred = async { alarmRepository.getAlarms(category = "CONTENT", page = 0, size = 50) }

            val noticeResult = noticeDeferred.await()
            val eventResult = eventDeferred.await()
            val contentResult = contentDeferred.await()

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

            contentResult.onSuccess { data ->
                Log.d("NoticeEventFlow", "✅ [목록 성공] 콘텐츠 수신: ${data.items.size}개")
            }.onFailure { error ->
                Log.e("NoticeEventFlow", "❌ [목록 실패] 콘텐츠 에러: ${error.message}")
            }

            val notices = noticeResult.getOrNull()?.items?.map { it.toNoticeEventItem("공지사항") } ?: emptyList()
            val events = eventResult.getOrNull()?.items?.map { it.toNoticeEventItem("이벤트") } ?: emptyList()
            val contents = contentResult.getOrNull()?.items?.map { it.toNoticeEventItem("콘텐츠") } ?: emptyList()

            _uiState.update {
                it.copy(
                    noticeList = notices,
                    eventList = events,
                    contentList = contents,
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
                    eventList = state.eventList.map { if (it.id == targetId) it.copy(isRead = true) else it },
                    contentList = state.contentList.map { if (it.id == targetId) it.copy(isRead = true) else it }
                )
            }

            val result = alarmRepository.getAlarmDetail(notificationId)

            result.onSuccess { detailData ->
                Log.d("NoticeEventFlow", "✅ [상세 성공] 받아온 데이터: $detailData")
            }.onFailure { error ->
                Log.e("NoticeEventFlow", "❌ [상세 실패] 에러 발생: ${error.message}")
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
}