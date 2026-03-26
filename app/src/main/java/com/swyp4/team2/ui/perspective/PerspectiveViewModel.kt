package com.swyp4.team2.ui.perspective

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.repository.PerspectiveRepository
import com.swyp4.team2.ui.perspective.model.PerspectiveUiModel
import com.swyp4.team2.ui.perspective.model.mockPerspectiveList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PerspectiveUiState(
    val battleId: Long = 0L,
    val perspectives: List<PerspectiveUiModel> = emptyList(),
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val isLoading: Boolean = false
)

@HiltViewModel
class PerspectiveViewModel @Inject constructor(
    private val repository: PerspectiveRepository
): ViewModel() {
//    private val _uiState = MutableStateFlow(PerspectiveUiState())
//    val uiState: StateFlow<PerspectiveUiState> = _uiState.asStateFlow()

    private val _uiState = MutableStateFlow(
        PerspectiveUiState(
            perspectives = mockPerspectiveList,
            hasNext = false
        )
    )
    val uiState: StateFlow<PerspectiveUiState> = _uiState.asStateFlow()

    fun loadPerspectives(isRefresh: Boolean = false) {
        val state = _uiState.value
        if (state.isLoading || (!isRefresh && !state.hasNext)) return

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            val cursor = if (isRefresh) null else state.nextCursor

            repository.getPerspectives(battleId = 1L, cursor = cursor, size = 10)
                .onSuccess { page ->
                    // val newItems = page.items.map { it.toUiModel() } // TODO: 매퍼 연결
                    val newItems = emptyList<PerspectiveUiModel>()

                    _uiState.update {
                        it.copy(
                            perspectives = if (isRefresh) newItems else it.perspectives + newItems,
                            nextCursor = page.nextCursor,
                            hasNext = page.hasNext,
                            isLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}