package com.picke.app.ui.recommend

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.RecommendBoard
import com.picke.app.domain.repository.RecommendRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecommendUiModel(
    val battleId: String,
    val title: String,
    val summary: String,
    val audioDuration: Int,
    val viewCount: Int,
    val participantsCount: Int,
    val tags: List<String>,
    val imageA: String,
    val imageB: String,
    val stanceA: String,
    val stanceB: String,
    val representativeA: String,
    val representativeB: String
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
                    Log.d("RecommendFlow", "🟢 추천 배틀 목록 조회 성공: ${page.items.size}개")

                    page.items.forEachIndexed { index, board ->
                        val optA = board.options.find { it.label == "A" || it.label == "AGREE" }
                        val optB = board.options.find { it.label == "B" || it.label == "DISAGREE" }

                        Log.d("RecommendFlow", """
                            --- [추천 배틀 Item $index] ---
                            battleId: ${board.battleId}
                            title: ${board.title}
                            summary: ${board.summary}
                            participantsCount: ${board.participantsCount}
                            tags: ${board.tags.map { it.name }}
                            
                            [Option A 데이터]
                            - label: ${optA?.label}
                            - title: ${optA?.title}
                            - stance: ${optA?.stance}
                            - representative(철학자): ${optA?.representative}
                            - imageUrl: ${optA?.imageUrl}
                            
                            [Option B 데이터]
                            - label: ${optB?.label}
                            - title: ${optB?.title}
                            - stance: ${optB?.stance}
                            - representative(철학자): ${optB?.representative}
                            - imageUrl: ${optB?.imageUrl}
                            -----------------------------
                        """.trimIndent())
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

// Domain Model -> UI Model
private fun RecommendBoard.toUiModel(): RecommendUiModel {
    val optA = this.options.find { it.label == "A" || it.label == "AGREE" }
    val optB = this.options.find { it.label == "B" || it.label == "DISAGREE" }
    val durationInMinutes = if (this.audioDuration > 0 && this.audioDuration < 60) {
        1
    } else {
        this.audioDuration / 60
    }
    return RecommendUiModel(
        battleId = this.battleId.toString(),
        title = this.title,
        summary = this.summary,
        audioDuration = durationInMinutes,
        viewCount = this.viewCount,
        participantsCount = this.participantsCount,
        tags = this.tags.map { it.name },
        imageA = optA?.imageUrl ?: "",
        imageB = optB?.imageUrl ?: "",
        stanceA = optA?.stance ?: "",
        stanceB = optB?.stance ?: "",
        representativeA = optA?.representative ?: "",
        representativeB = optB?.representative ?: ""
    )
}