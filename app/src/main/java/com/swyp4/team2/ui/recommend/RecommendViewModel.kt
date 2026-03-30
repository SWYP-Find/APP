package com.swyp4.team2.ui.recommend

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.RecommendBoard
import com.swyp4.team2.domain.repository.RecommendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecommendUiModel(
    val id: String,
    val profileImageUrl: String,
    val nickname: String,
    val stance: String,
    val content: String,
    val likeCount: Int,
    val isLiked: Boolean,
    val isMine: Boolean,
    val timeAgo: String
)

data class RecommendUiState(
    val battleId: String = "",
    val recommendList: List<RecommendUiModel> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class RecommendViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recommendRepository: RecommendRepository
) : ViewModel() {

    // 이전 화면에서 넘겨받은 battleId
    private val receivedBattleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _uiState = MutableStateFlow(RecommendUiState(battleId = receivedBattleId))
    val uiState: StateFlow<RecommendUiState> = _uiState.asStateFlow()

    init {
        loadRecommendations()
    }

    private fun loadRecommendations() {
        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            recommendRepository.getInterestingRecommendations(battleIdLong)
                .onSuccess { page ->
                    Log.d("RecommendFlow", "🟢 추천 목록 조회 성공: ${page.items.size}개")
                    val uiModels = page.items.map { it.toUiModel() }

                    _uiState.update {
                        it.copy(
                            recommendList = uiModels,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("RecommendFlow", "🔴 추천 목록 로드 실패: ${error.message}", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}

// Domain -> UI
private fun RecommendBoard.toUiModel(): RecommendUiModel {
    return RecommendUiModel(
        id = this.commentId.toString(),
        profileImageUrl = this.user.characterImageUrl,
        nickname = this.user.nickname,
        stance = if (this.stance == "A" || this.stance == "AGREE") "찬성" else "반대",
        content = this.content,
        likeCount = this.likeCount,
        isLiked = this.isLiked,
        isMine = this.isMine,
        timeAgo = this.createdAt
    )
}