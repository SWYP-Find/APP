package com.swyp4.team2.ui.my.philosopher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.CategoryCount
import com.swyp4.team2.domain.model.Chemistry
import com.swyp4.team2.domain.model.ChemistryPhilosopher
import com.swyp4.team2.domain.model.MainPhilosopherDetail
import com.swyp4.team2.domain.model.MyRecapBoard
import com.swyp4.team2.domain.model.PhilosopherReport
import com.swyp4.team2.domain.model.TasteReport
import com.swyp4.team2.domain.model.TraitAnalysis
import com.swyp4.team2.domain.repository.MyPageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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

    /*fun fetchOtherPhilosopherReport(reportId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // TODO: 실제 서버 API(GET /reports/{reportId} 등) 호출로 교체해야 함!
            delay(1000)

            val otherDummyReport = PhilosopherReport(
                reportId = reportId,
                hasTestResult = true,
                mainPhilosopher = MainPhilosopherDetail(
                    name = "니체형",
                    description = "기존의 가치를 파괴하고 초인을 꿈꾸는 사람. 남의 시선에 얽매이지 않고 자신만의 길을 개척합니다.",
                    imageUrl = R.drawable.ic_profile_niche,
                    tags = listOf("초인", "독립적", "도전적", "자유로움")
                ),
                traitAnalysis = TraitAnalysis(
                    principle = 30,
                    logic = 60,
                    individual = 95,
                    change = 85,
                    inner = 90,
                    ideal = 50
                ),
                tasteReport = TasteReport(
                    totalParticipation = 120,
                    opinionChanges = 5,
                    winRate = 85,
                    topCategories = listOf(
                        CategoryCount("자유", 50),
                        CategoryCount("도전", 35),
                        CategoryCount("예술", 20),
                        CategoryCount("사회", 15)
                    )
                ),
                chemistry = Chemistry(
                    best = ChemistryPhilosopher(
                        name = "사르트르형",
                        description = "자유와 책임을 중시하는 실존주의자.",
                        imageUrl = R.drawable.ic_profile_mengzi
                    ),
                    worst = ChemistryPhilosopher(
                        name = "칸트형",
                        description = "결과보다 과정을 중시하고 보편적 도덕 법칙을 따르는 원칙주의자.",
                        imageUrl = R.drawable.ic_profile_kant
                    )
                )
            )

            _uiState.update {
                it.copy(
                    recapBoard = otherDummyReport,
                    isLoading = false
                )
            }
        }
    }*/
}