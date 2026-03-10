package com.swyp4.team2.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
) : ViewModel() {
    private val _nickname = MutableStateFlow("생각하는 갈대")
    val nickname: StateFlow<String> = _nickname.asStateFlow()

    private val _selectedCharacterId = MutableStateFlow<Int?>(null)
    val selectedCharacterId: StateFlow<Int?> = _selectedCharacterId.asStateFlow()

    fun selectCharacter(characterId: Int){
        _selectedCharacterId.value = characterId
    }

    fun submitProfile(onSuccess:()->Unit){
        val characterId = _selectedCharacterId.value ?: return

        viewModelScope.launch{
            // 백엔드에 PATCH 요청 보내기
        }

    }
}