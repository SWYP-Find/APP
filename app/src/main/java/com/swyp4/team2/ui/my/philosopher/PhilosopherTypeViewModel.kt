package com.swyp4.team2.ui.my.philosopher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.CategoryCount
import com.swyp4.team2.domain.model.Chemistry
import com.swyp4.team2.domain.model.ChemistryPhilosopher
import com.swyp4.team2.domain.model.MainPhilosopherDetail
import com.swyp4.team2.domain.model.PhilosopherReport
import com.swyp4.team2.domain.model.TasteReport
import com.swyp4.team2.domain.model.TraitAnalysis
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PhilosopherTypeUiState(
    val report: PhilosopherReport? = null,
    val isLoading: Boolean = false
)

@HiltViewModel
class PhilosopherTypeViewModel @Inject constructor() : ViewModel(){
    private val _uiState = MutableStateFlow(PhilosopherTypeUiState())
    val uiState: StateFlow<PhilosopherTypeUiState> = _uiState.asStateFlow()

    init {
        fetchPhilosopherReport()
    }

    private fun fetchPhilosopherReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // TODO: 실제 서버 API(GET /my/philosopher-report) 호출로 교체해야 함!
            delay(1000)

            val dummyReport = PhilosopherReport(
                hasTestResult = true,
                mainPhilosopher = MainPhilosopherDetail(
                    name = "칸트형",
                    description = "결과보다 과정을 중시하고, 보편적 도덕 법칙을 따르는 원칙주의자. 어떤 상황에서도 흔들리지 않는 기준을 가진 사람입니다.",
                    imageUrl = R.drawable.ic_profile_kant,
                    tags = listOf("의무론", "규칙 중시", "보편 원칙", "이상적")
                ),
                traitAnalysis = TraitAnalysis(
                    principle = 92,
                    logic = 85,
                    individual = 72,
                    change = 38,
                    inner = 88,
                    ideal = 45
                ),
                tasteReport = TasteReport(
                    totalParticipation = 47,
                    opinionChanges = 12,
                    winRate = 68,
                    topCategories = listOf(
                        CategoryCount("철학", 20),
                        CategoryCount("문학", 13),
                        CategoryCount("예술", 8),
                        CategoryCount("사회", 5)
                    )
                ),
                chemistry = Chemistry(
                    best = ChemistryPhilosopher(
                        name = "플라톤형",
                        description = "본질과 이상을 좇으며 더 나은 세계를 꿈꾸는 사람.",
                        imageUrl = R.drawable.ic_profile_mengzi
                    ),
                    worst = ChemistryPhilosopher(
                        name = "니체형",
                        description = "기존의 틀을 부수고 자기만의 길을 가는 사람",
                        imageUrl = R.drawable.ic_profile_niche
                    )
                )
            )

            _uiState.update {
                it.copy(
                    report = dummyReport,
                    isLoading = false
                )
            }
        }
    }
}