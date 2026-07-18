package com.picke.app.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.app.domain.usecase.FetchHomeDataUseCase
import com.picke.app.domain.usecase.GetTodayPickVoteUseCase
import com.picke.app.domain.usecase.GetUnreadAlarmStatusUseCase
import com.picke.app.domain.usecase.SubmitTodayPickVoteUseCase
import com.picke.app.util.ContentType
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
    // 미읽음 알림 여부 조회가 끝나기 전까지 탑바 아이콘을 shimmer로 보여주기 위한 플래그
    val isAlarmStatusLoading: Boolean = true,
    val editorPicks: List<HomeContentUiModel> = emptyList(),
    val trendingBattles: List<HomeContentUiModel> = emptyList(),
    val bestBattles: List<HomeContentUiModel> = emptyList(),
    val newBattles: List<HomeContentUiModel> = emptyList(),
    val todayPicks: List<TodayPickUiModel> = emptyList()
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val fetchHomeDataUseCase: FetchHomeDataUseCase,
    private val submitTodayPickVoteUseCase: SubmitTodayPickVoteUseCase,
    private val getTodayPickVoteUseCase: GetTodayPickVoteUseCase,
    private val getUnreadAlarmStatusUseCase: GetUnreadAlarmStatusUseCase
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

            fetchHomeDataUseCase()
                .onSuccess { boardData ->
                    Log.d("HomeFlow", "2. 🟢 홈 데이터 통신 성공!")

                    // 오늘의 Pické(투표/퀴즈) 섹션은 홈에서 제거하기로 하여 데이터를 세팅하지 않는다.
                    // (백엔드가 내려주더라도 홈에서는 사용하지 않으므로 투표 내역 동기화도 생략)
                    // 필요 시 아래 initialTodayPicks 세팅과 syncTodayPicksVotes 호출을 복원하면 된다.
                    // val initialTodayPicks = boardData.todayPicks.map { it.toUiModel() }

                    _uiState.update { state ->
                        state.copy(
                            isLoading = false,
                            editorPicks = boardData.editorPicks.map { it.toUiModel() },
                            trendingBattles = boardData.trendingBattles.map { it.toUiModel() },
                            bestBattles = boardData.bestBattles.map { it.toUiModel() },
                            newBattles = boardData.newBattles.map { it.toUiModel() },
                            todayPicks = emptyList()
                        )
                    }

                    // 2차로 투표 내역(GET /me)을 불러와서 덮어씌우기 (오늘의 Pické 제거로 비활성화)
                    // syncTodayPicksVotes(initialTodayPicks)
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

                getTodayPickVoteUseCase(battleIdLong, pick.type).fold(
                    onSuccess = { voteBoard ->
                        Log.d("HomeFlow", "   - 📥 [Response] 내역 동기화 성공: battleId=$battleIdLong")
                        Log.d("HomeFlow", "      ㄴ 받아온 진짜 optionId 통계: ${voteBoard.stats}")

                        when (pick) {
                            is TodayPickUiModel.VotePick -> pick.copy(
                                selectedOptionId = voteBoard.selectedOptionId,
                                options = voteBoard.stats.sortedBy { it.optionId }.map { it.toUiModel() },
                                participantsCount = voteBoard.totalCount
                            )
                            is TodayPickUiModel.QuizPick -> pick.copy(
                                selectedOptionId = voteBoard.selectedOptionId,
                                options = voteBoard.stats.sortedBy { it.optionId }.map { it.toUiModel() },
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

    // 미읽음 알림 존재 여부 조회 (탑바 벨 아이콘 빨간 점 배지)
    fun fetchUnreadAlarmStatus() {
        // 재진입 시에도 배지 여부가 확정되기 전까지 아이콘을 shimmer로 유지한다.
        _uiState.update { it.copy(isAlarmStatusLoading = true) }

        viewModelScope.launch {
            getUnreadAlarmStatusUseCase()
                .onSuccess { hasUnread ->
                    _uiState.update { it.copy(hasNewNotice = hasUnread, isAlarmStatusLoading = false) }
                }
                .onFailure { error ->
                    Log.e("HomeFlow", "🔴 미읽음 알림 여부 조회 실패!", error)
                    _uiState.update { it.copy(isAlarmStatusLoading = false) }
                }
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

            submitTodayPickVoteUseCase(battleIdLong, optionId, type).onSuccess { voteBoard ->
                Log.d("HomeFlow", "✅ 📥 [Response] 투표/퀴즈 제출 성공!")
                Log.d("HomeFlow", "   - 서버가 인정한 내 선택: ${voteBoard.selectedOptionId}")
                Log.d("HomeFlow", "   - 서버가 내려준 최신 통계: ${voteBoard.stats}")

                _uiState.update { state ->
                    val updatedPicks = state.todayPicks.map { pick ->
                        if (pick.contentId == battleId) {
                            when (pick) {
                                is TodayPickUiModel.VotePick -> pick.copy(
                                    selectedOptionId = voteBoard.selectedOptionId,
                                    options = voteBoard.stats.sortedBy { it.optionId }.map { it.toUiModel() },
                                    participantsCount = pick.participantsCount + 1
                                )
                                is TodayPickUiModel.QuizPick -> pick.copy(
                                    selectedOptionId = voteBoard.selectedOptionId,
                                    options = voteBoard.stats.sortedBy { it.optionId }.map { it.toUiModel() },
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