package com.swyp4.team2.ui.my.discussion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.DiscussionHistoryItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiscussionHistoryUiState(
    val agreeList: List<DiscussionHistoryItem> = emptyList(),
    val disagreeList: List<DiscussionHistoryItem> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class DiscussionHistoryViewModel @Inject constructor() : ViewModel(){
    private val _uiState = MutableStateFlow(DiscussionHistoryUiState())
    val uiState: StateFlow<DiscussionHistoryUiState> = _uiState.asStateFlow()

    init {
        fetchDiscussionHistory()
    }
    private fun fetchDiscussionHistory() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            delay(1000)

            val dummyAgreeList = listOf(
                DiscussionHistoryItem(1, "철학", "인간의 본성은 선한가 악한가?", "저는 맹자의 성선설에 전적으로 동의합니다. 인간은 태어날 때부터...", "2026.03.15"),
                DiscussionHistoryItem(2, "사회", "기본소득제 도입, 필요한가?", "기본소득제는 현대 사회의 양극화를 해소할 수 있는...", "2026.03.12")
            )

            val dummyDisagreeList = listOf(
                DiscussionHistoryItem(3, "기술", "AI 발전은 인류에게 위협인가?", "AI는 도구일 뿐, 그것을 사용하는 인간의 윤리적 잣대가...", "2026.03.10")
            )

            _uiState.update {
                it.copy(
                    agreeList = dummyAgreeList,
                    disagreeList = dummyDisagreeList,
                    isLoading = false
                )
            }
        }
    }
}