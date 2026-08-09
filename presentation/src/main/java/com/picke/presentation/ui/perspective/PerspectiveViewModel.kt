package com.picke.presentation.ui.perspective

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.perspective.usecase.PerspectiveUseCases
import com.picke.domain.feature.perspective.usecase.ReportPerspectiveResult
import com.picke.domain.feature.pollquiz.model.PollQuizVoteBoard
import com.picke.domain.feature.vote.usecase.VoteUseCases
import com.picke.presentation.ui.perspective.model.PerspectiveUiEvent
import com.picke.presentation.ui.perspective.model.PerspectiveUiState
import com.picke.presentation.ui.perspective.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerspectiveViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val perspectiveUseCases: PerspectiveUseCases,
    private val voteUseCases: VoteUseCases
) : ViewModel() {

    private val receivedBattleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _uiState = MutableStateFlow(
        PerspectiveUiState(
            battleId = receivedBattleId,
            perspectives = emptyList(),
            hasNext = true
        )
    )
    val uiState: StateFlow<PerspectiveUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<PerspectiveUiEvent>()
    val uiEvent: SharedFlow<PerspectiveUiEvent> = _uiEvent.asSharedFlow()

    private val _realTimeStats = MutableStateFlow<PollQuizVoteBoard?>(null)
    val realTimeStats: StateFlow<PollQuizVoteBoard?> = _realTimeStats.asStateFlow()

    init {
        loadPerspectives()
        loadVoteStats()
        loadMyPerspective()
        loadMyVoteHistory()
    }

    private fun loadMyVoteHistory() {
        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            voteUseCases.getMyVoteHistoryUseCase(battleIdLong)
                .onSuccess { voteHistory ->
                    _uiState.update {
                        it.copy(
                            opinionChanged = voteHistory.opinionChanged,
                            battleTitle = voteHistory.battleTitle
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(opinionChanged = false) }
                }
        }
    }

    private fun loadMyPerspective() {
        viewModelScope.launch {
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
            perspectiveUseCases.getMyPerspectiveUseCase(battleIdLong)
                .onSuccess { myData ->
                    _uiState.update { it.copy(myPerspective = myData) }
                }
                .onFailure {
                    _uiState.update { it.copy(myPerspective = null) }
                }
        }
    }

    fun updateSort(newSort: String) {
        if (_uiState.value.sort == newSort) return

        _uiState.update {
            it.copy(
                sort = newSort,
                nextCursor = null,
                hasNext = true,
                perspectives = emptyList()
            )
        }
        loadPerspectives(isRefresh = true)
    }

    fun selectOption(optionId: Long?) {
        if (_uiState.value.selectedOptionId == optionId) return

        _uiState.update {
            it.copy(
                selectedOptionId = optionId,
                nextCursor = null,
                hasNext = true,
                perspectives = emptyList()
            )
        }
        loadPerspectives(isRefresh = true)
    }

    fun loadPerspectives(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (!isRefresh && (state.isLoading || !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor
            val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L

            perspectiveUseCases.loadPerspectivesUseCase(
                battleId = battleIdLong,
                cursor = cursor,
                size = 10,
                optionId = state.selectedOptionId,
                sort = state.sort
            ).onSuccess { page ->
                val newItems = page.items.map { it.toUiModel() }
                val actualHasNext = page.hasNext && page.nextCursor != null && newItems.isNotEmpty()

                _uiState.update { currentState ->
                    val mergedList =
                        if (isRefresh) newItems else (currentState.perspectives + newItems).distinctBy { it.commentId }
                    currentState.copy(
                        perspectives = mergedList,
                        nextCursor = page.nextCursor,
                        hasNext = actualHasNext,
                        isLoading = false
                    )
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private fun loadVoteStats() {
        viewModelScope.launch {
            voteUseCases.getVoteStatsUseCase(receivedBattleId.toLong())
                .onSuccess { statsBoard ->
                    _uiState.update { it.copy(voteOptions = statsBoard.options) }
                }
        }
    }

    fun setEditMode(perspectiveId: Long?) {
        _uiState.update { it.copy(editingPerspectiveId = perspectiveId) }
    }

    fun submitPerspective(content: String, onSuccess: () -> Unit) {
        if (content.isBlank()) return

        val battleIdLong = receivedBattleId.toLongOrNull() ?: 0L
        val editId = _uiState.value.editingPerspectiveId
        val isEditMode = editId != null

        _uiState.update { it.copy(editingPerspectiveId = null) }

        viewModelScope.launch {
            perspectiveUseCases.submitPerspectiveUseCase(battleIdLong, editId, content)
                .onSuccess {
                    onSuccess()
                    loadMyPerspective()
                    loadPerspectives(isRefresh = true)
                }
                .onFailure {
                    loadMyPerspective()
                }
        }
    }

    fun deletePerspective(perspectiveId: Long) {
        _uiState.update { state ->
            state.copy(
                perspectives = state.perspectives.filter { it.commentId != perspectiveId.toString() },
                myPerspective = if (state.myPerspective?.perspectiveId == perspectiveId) null else state.myPerspective
            )
        }

        viewModelScope.launch {
            perspectiveUseCases.deletePerspectiveUseCase(perspectiveId)
                .onFailure {
                    loadMyPerspective()
                    loadPerspectives(isRefresh = true)
                }
        }
    }

    fun toggleLike(perspectiveId: Long, isCurrentlyLiked: Boolean) {
        viewModelScope.launch {
            val action = if (isCurrentlyLiked) "취소" else "등록"

            perspectiveUseCases.togglePerspectiveLikeUseCase(perspectiveId, isCurrentlyLiked)
                .onSuccess { toggleData ->
                    _uiState.update { state ->
                        state.copy(
                            perspectives = state.perspectives.map { item ->
                                if (item.commentId == perspectiveId.toString()) {
                                    item.copy(
                                        likeCount = toggleData.likeCount,
                                        isLiked = toggleData.isLiked
                                    )
                                } else {
                                    item
                                }
                            }
                        )
                    }
                }
        }
    }

    fun reportPerspective(perspectiveId: Long) {
        viewModelScope.launch {
            perspectiveUseCases.reportPerspectiveUseCase(perspectiveId)
                .onSuccess { result ->
                    when (result) {
                        is ReportPerspectiveResult.Reported -> {
                            _uiEvent.emit(PerspectiveUiEvent.ShowToast("신고가 정상 접수되었습니다."))
                        }

                        is ReportPerspectiveResult.AlreadyReported -> {
                            _uiEvent.emit(PerspectiveUiEvent.ShowToast("이미 신고한 사용자입니다."))
                        }
                    }
                }
        }
    }

    fun retryModeration(perspectiveId: Long) {
        viewModelScope.launch {
            perspectiveUseCases.retryModerationUseCase(perspectiveId)
                .onSuccess {
                    loadMyPerspective()
                }
        }
    }

    fun refreshAllData() {
        loadMyVoteHistory()
        loadVoteStats()
        loadMyPerspective()
        loadPerspectives(isRefresh = true)
    }
}