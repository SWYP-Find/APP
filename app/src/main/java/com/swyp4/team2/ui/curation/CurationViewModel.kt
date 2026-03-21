package com.swyp4.team2.ui.curation

import androidx.lifecycle.ViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.home.model.BattleProfile
import com.swyp4.team2.ui.home.model.NewBattleItem
import com.swyp4.team2.ui.home.model.dummyNewBattleList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CurationUiState(
    val curationList: List<NewBattleItem> = emptyList(),
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
        // TODO: 나중에 서버 API(GET)를 호출해서 데이터를 받아오는 로직으로 교체합니다.
        val dummyData = dummyNewBattleList

        // 상태 업데이트
        _uiState.update { currentState ->
            currentState.copy(
                curationList = dummyData,
                isLoading = false
            )
        }
    }
}