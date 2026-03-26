package com.swyp4.team2.ui.curation

import androidx.lifecycle.ViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.home.model.BattleProfile
import com.swyp4.team2.ui.home.model.ContentUiType
import com.swyp4.team2.ui.home.model.HomeContentUiModel
import com.swyp4.team2.ui.home.model.NewBattleItem
import com.swyp4.team2.ui.home.model.dummyNewBattleList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CurationUiState(
    val curationList: List<HomeContentUiModel> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class CurationViewModel @Inject constructor() : ViewModel() {

    // 외부에서 읽기만 가능한 StateFlow
    private val _uiState = MutableStateFlow(CurationUiState())
    val uiState: StateFlow<CurationUiState> = _uiState.asStateFlow()

    init {
        loadCurationData()
    }

    private fun loadCurationData() {
        val dummyData = dummyNewBattleList

        val uiModels = dummyData.map { item ->
            HomeContentUiModel(
                contentId = item.hashCode().toString(),
                type = ContentUiType.BATTLE,
                thumbnailUrl = "",
                title = item.title,
                summary = item.description,
                viewCountText = "726",
                timeInfoText = "5분",
                tags = listOf(item.category),
                leftOpinion = "악하다",
                leftProfileName = "순자",
                rightOpinion = "순하다",
                rightProfileName = "맹자"
            )
        }

        // 🌟 2. 버그 수정: dummyData 대신 방금 변환을 마친 uiModels를 넣습니다!
        _uiState.update { currentState ->
            currentState.copy(
                curationList = uiModels,
                isLoading = false
            )
        }
    }
}