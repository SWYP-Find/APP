package com.picke.app.ui.my.philosopher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.model.MyRecapBoard
import com.picke.app.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PhilosopherTypeUiState(
    val recapBoard: MyRecapBoard? = null,
    val isLoading: Boolean = false,
    val isLocked: Boolean = false
)

@HiltViewModel
class PhilosopherTypeViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
) : ViewModel(){
    private val _uiState = MutableStateFlow(PhilosopherTypeUiState())
    val uiState: StateFlow<PhilosopherTypeUiState> = _uiState.asStateFlow()

    fun fetchPhilosopherReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = myPageRepository.getMyRecap()

            result.onSuccess { data ->
                _uiState.update {
                    it.copy(
                        recapBoard = data,
                        isLoading = false,
                        isLocked = false
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        recapBoard = null,
                        isLoading = false,
                        isLocked = true
                    )
                }
            }
        }
    }
}