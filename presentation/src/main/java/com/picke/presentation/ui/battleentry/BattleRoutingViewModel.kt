package com.picke.presentation.ui.battleentry

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.feature.battle.usecase.BattleUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BattleRoutingViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val battleUseCases: BattleUseCases
) : ViewModel() {

    val battleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _routeEvent = MutableStateFlow<String?>(null)
    val routeEvent: StateFlow<String?> = _routeEvent.asStateFlow()

    init {
        checkBattleStatus()
    }

    private fun checkBattleStatus() {
        viewModelScope.launch {
            battleUseCases.getBattleStatusUseCase(battleId.toLongOrNull() ?: 0L)
                .onSuccess { statusBoard ->
                    when (statusBoard.step) {
                        "COMPLETED" -> _routeEvent.value = "PERSPECTIVE"
                        "NONE", "PRE_VOTE", "SCENARIO" -> _routeEvent.value = "PRE_VOTE"
                        else -> _routeEvent.value = "PRE_VOTE"
                    }
                }
                .onFailure {
                    _routeEvent.value = "PRE_VOTE"
                }
        }
    }
}
