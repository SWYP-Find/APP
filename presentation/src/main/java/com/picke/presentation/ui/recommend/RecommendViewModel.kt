package com.picke.presentation.ui.recommend

import android.util.Log
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
                    Log.d("RecommendFlow", "🟢 추천 배틀 목록 조회 성공: ${page.items.size}개")

                    page.items.forEachIndexed { index, board ->
                        val optA = board.options.getOrNull(0)
                        val optB = board.options.getOrNull(1)

                        Log.d(
                            "RecommendFlow", """
                            --- [추천 배틀 Item $index] ---
                            battleId: ${board.battleId}
                            title: ${board.title}
                            summary: ${board.summary}
                            participantsCount: ${board.participantsCount}
                            tags: ${board.tags.map { it.name }}

                            [Option A 데이터]
                            - title: ${optA?.title}
                            - stance: ${optA?.stance}
                            - representative(철학자): ${optA?.representative}
                            - imageUrl: ${optA?.imageUrl}

                            [Option B 데이터]
                            - title: ${optB?.title}
                            - stance: ${optB?.stance}
                            - representative(철학자): ${optB?.representative}
                            - imageUrl: ${optB?.imageUrl}
                            -----------------------------
                        """.trimIndent()
                        )
                    }

                    val uiModels = page.items.map { it.toUiModel() }

                    _uiState.update {
                        it.copy(
                            recommendList = uiModels,
                            isLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    Log.e("RecommendFlow", "🔴 추천 배틀 목록 로드 실패: ${error.message}", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }
}