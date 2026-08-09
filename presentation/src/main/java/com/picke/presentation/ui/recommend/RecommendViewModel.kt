package com.picke.presentation.ui.recommend

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.recommend.usecase.RecommendUseCases
import com.picke.presentation.ui.recommend.model.RecommendUiState
import com.picke.presentation.ui.recommend.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val recommendUseCases: RecommendUseCases
) : ViewModel() {

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
            recommendUseCases.getInterestingRecommendationsUseCase(battleIdLong)
                .onSuccess { page ->
                    val uiModels = page.items.map { it.toUiModel() }

                    _uiState.update {
                        it.copy(
                            recommendList = uiModels,
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