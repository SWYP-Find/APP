package com.picke.app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.repository.HomeRepository
import com.picke.app.domain.repository.PollQuizRepository
import com.picke.app.ui.home.model.HomeContentUiModel
import com.picke.app.ui.home.model.TodayPickUiModel
import com.picke.app.ui.home.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = true,
    val hasNewNotice: Boolean = false,
    val editorPicks: List<HomeContentUiModel> = emptyList(),
    val trendingBattles: List<HomeContentUiModel> = emptyList(),
    val bestBattles: List<HomeContentUiModel> = emptyList(),
    val newBattles: List<HomeContentUiModel> = emptyList(),
    val todayPicks: List<TodayPickUiModel> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val pollQuizRepository: PollQuizRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(isLoading = true))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchHomeData()
    }

    // 홈 데이터 불러오기
    fun fetchHomeData() {
        Log.d("HomeFlow", "1. 홈 데이터 서버에 요청 시작!")
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            homeRepository.fetchHomeData()
                .onSuccess { boardData ->
                    Log.d("HomeFlow", "2. 🟢 홈 데이터 통신 성공!")

                    // 1차: 기본 홈 데이터 세팅
                    val initialTodayPicks = boardData.todayPicks.map { it.toUiModel() }

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            hasNewNotice = boardData.hasNewNotice,
                            editorPicks = boardData.editorPicks.map { it.toUiModel() },
                            trendingBattles = boardData.trendingBattles.map { it.toUiModel() },
                            bestBattles = boardData.bestBattles.map { it.toUiModel() },
                            newBattles = boardData.newBattles.map { it.toUiModel() },
                            todayPicks = initialTodayPicks
                        )
                    }

                    // 2차로 투표 내역(GET /me)을 불러와서 덮어씌우기
                    syncTodayPicksVotes(initialTodayPicks)
                }
                .onFailure { error ->
                    Log.e("HomeFlow", "2. 🔴 홈 데이터 통신 실패!", error)
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    // 투표 내역 동기화 로직
    private suspend fun syncTodayPicksVotes(picks: List<TodayPickUiModel>) {
        if (picks.isEmpty()) {
            Log.d("HomeFlow", "3. [Sync] 동기화할 TodayPicks가 없어서 종료합니다.")
            return
        }

        Log.d("HomeFlow", "3. [Sync] 총 ${picks.size}개의 Pick에 대해 투표 내역 조회를 시작합니다.")

        val deferredResults = picks.map { pick ->
            viewModelScope.async {
                val battleIdLong = pick.contentId.toLongOrNull() ?: return@async pick

                Log.d("HomeFlow", "   - 📤 [Request] 동기화 요청: battleId=$battleIdLong, type=${pick.type}")

                val result = if (pick.type == "QUIZ") {
                    pollQuizRepository.getMyQuizVote(battleIdLong)
                } else {
                    pollQuizRepository.getMyPollVote(battleIdLong)
                }

                result.fold(
                    onSuccess = { voteBoard ->
                        Log.d("HomeFlow", "   - 📥 [Response] 내역 동기화 성공: battleId=$battleIdLong")
                        Log.d("HomeFlow", "      ㄴ 받아온 진짜 optionId 통계: ${voteBoard.stats}")

                        when (pick) {
                            is TodayPickUiModel.VotePick -> pick.copy(
                                selectedOptionId = voteBoard.selectedOptionId,
                                options = voteBoard.stats.sortedBy { it.label }.map { it.toUiModel() },
                                participantsCount = voteBoard.totalCount
                            )
                            is TodayPickUiModel.QuizPick -> pick.copy(
                                selectedOptionId = voteBoard.selectedOptionId,
                                options = voteBoard.stats.sortedBy { it.label }.map { it.toUiModel() },
                                participantsCount = voteBoard.totalCount
                            )
                        }
                    },
                    onFailure = { error ->
                        Log.e("HomeFlow", "   - ❌ [Error] 투표 내역 API 호출 실패: battleId=$battleIdLong")
                        Log.e("HomeFlow", "      ㄴ 원인(메시지): ${error.message}", error)
                        pick
                    }
                )
            }
        }

        val updatedPicks = deferredResults.awaitAll()
        Log.d("HomeFlow", "4. [Sync] 모든 투표 내역 동기화 완료! UI를 갱신합니다.")

        _uiState.update { state ->
            state.copy(todayPicks = updatedPicks)
        }
    }

    // 투표/퀴즈 제출 로직
    fun submitTodayPickVote(battleId: String, optionId: Long, type: String) {
        viewModelScope.launch {
            val battleIdLong = battleId.toLongOrNull() ?: return@launch

            Log.d("HomeFlow", "▶️ 📤 [Request] 오늘의 Pické 투표 제출 API 호출")
            Log.d("HomeFlow", "   - 보낸 데이터 (배틀ID): $battleIdLong")
            Log.d("HomeFlow", "   - 보낸 데이터 (타입): $type")
            Log.d("HomeFlow", "   - 보낸 데이터 (선택한옵션ID): $optionId")

            val result = if (type == "QUIZ") {
                pollQuizRepository.submitQuizVote(battleIdLong, optionId)
            } else {
                pollQuizRepository.submitPollVote(battleIdLong, optionId)
            }

            result.onSuccess { voteBoard ->
                Log.d("HomeFlow", "✅ 📥 [Response] 투표/퀴즈 제출 성공!")
                Log.d("HomeFlow", "   - 서버가 인정한 내 선택: ${voteBoard.selectedOptionId}")
                Log.d("HomeFlow", "   - 서버가 내려준 최신 통계: ${voteBoard.stats}")

                _uiState.update { state ->
                    val updatedPicks = state.todayPicks.map { pick ->
                        if (pick.contentId == battleId) {
                            when (pick) {
                                is TodayPickUiModel.VotePick -> pick.copy(
                                    selectedOptionId = voteBoard.selectedOptionId,
                                    options = voteBoard.stats.sortedBy { it.label }.map { it.toUiModel() },
                                    participantsCount = pick.participantsCount + 1
                                )
                                is TodayPickUiModel.QuizPick -> pick.copy(
                                    selectedOptionId = voteBoard.selectedOptionId,
                                    options = voteBoard.stats.sortedBy { it.label }.map { it.toUiModel() },
                                    participantsCount = pick.participantsCount + 1
                                )
                            }
                        } else pick
                    }
                    state.copy(todayPicks = updatedPicks)
                }
            }.onFailure { error ->
                Log.e("HomeFlow", "❌ [Error] 투표/퀴즈 제출 실패!")
                Log.e("HomeFlow", "   - 실패 원인(메시지): ${error.message}", error)
            }
        }
    }


}