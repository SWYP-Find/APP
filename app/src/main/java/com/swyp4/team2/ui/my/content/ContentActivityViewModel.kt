package com.swyp4.team2.ui.my.content

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.ContentActivityItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ContentActivityUiState(
    val commentList: List<ContentActivityItem> = emptyList(),
    val likeList: List<ContentActivityItem> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class ContentActivityViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(ContentActivityUiState())
    val uiState: StateFlow<ContentActivityUiState> = _uiState.asStateFlow()

    init {
        fetchContentActivity()
    }
    private fun fetchContentActivity() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            delay(1000)

            val dummyComments = listOf(
                ContentActivityItem("1", "사색하는 고양이", "찬성", "1시간 전", "저도 이 의견에 전적으로 동의합니다. 현실적인 대안이 필요해요.", 12),
                ContentActivityItem("2", "사색하는 고양이", "반대", "3시간 전", "그건 너무 이상적인 이야기 아닐까요? 당장 도입하기엔 무리가 있습니다.", 5)
            )

            val dummyLikes = listOf(
                ContentActivityItem("3", "지나가는 철학자", "찬성", "어제", "맞아요. 칸트의 의무론적 관점에서 보면 이건 무조건 지켜야 할 원칙이죠.", 128),
                ContentActivityItem("4", "현실주의자", "반대", "2일 전", "이론상으로는 좋지만, 실제 사회에 적용했을 때의 부작용도 고려해야 합니다.", 45)
            )

            _uiState.update {
                it.copy(
                    commentList = dummyComments,
                    likeList = dummyLikes,
                    isLoading = false
                )
            }
        }
    }
}