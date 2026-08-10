package com.picke.presentation.ui.vote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picke.domain.common.local.LocalPreferencesUseCases
import com.picke.domain.feature.battle.usecase.BattleUseCases
import com.picke.domain.feature.share.usecase.ShareUseCases
import com.picke.domain.feature.vote.usecase.SubmitVoteResult
import com.picke.domain.feature.vote.usecase.VoteUseCases
import com.picke.presentation.ads.AdMobManager
import com.picke.presentation.analytics.AnalyticsTracker
import com.picke.presentation.analytics.BattleStepName
import com.picke.presentation.analytics.ShareTarget
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.VoteUiState
import com.picke.presentation.ui.vote.model.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val battleUseCases: BattleUseCases,
    private val voteUseCases: VoteUseCases,
    private val shareUseCases: ShareUseCases,
    private val localPreferencesUseCases: LocalPreferencesUseCases,
    val adMobManager: AdMobManager,
    private val analyticsTracker: AnalyticsTracker
) : ViewModel() {

    companion object {
        private const val TAG = "VoteViewModel_Picke"
    }

    val battleId: String = checkNotNull(savedStateHandle["battleId"])

    private val _uiState = MutableStateFlow(VoteUiState(isLoading = true))
    val uiState: StateFlow<VoteUiState> = _uiState.asStateFlow()

    init {
        fetchVoteDetail()
        reloadAd()
    }

    fun reloadAd() {
        localPreferencesUseCases.getUserTag()?.let { tag ->
            Log.d(TAG, "[FLOW] 광고 리로드 시작. UserTag: $tag")
            adMobManager.loadAd(tag)
        } ?: Log.w(TAG, "[FLOW] UserTag 없음: 광고 리로드 스킵")
    }

    private fun fetchVoteDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val battleIdLong = battleId.toLongOrNull() ?: 0L
            Log.d(TAG, "[FLOW] 배틀 상세 정보 호출 시작. Battle ID: $battleIdLong")

            battleUseCases.getBattleDetailUseCase(battleIdLong)
                .onSuccess { detailBoard ->
                    Log.i(TAG, "[STATE] 배틀 상세 정보 로드 성공")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            battleDetail = detailBoard.toUiModel(),
                            error = null
                        )
                    }
                }
                .onFailure { error ->
                    Log.w(TAG, "[FLOW] 배틀 상세 정보 로드 실패: ${error.message}")
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message)
                    }
                }
        }
    }

    fun submitVote(
        voteType: VoteType,
        selectedOptionId: String,
        onSuccess: () -> Unit
    ) {
        // API 중복 요청 방지 등 필요 시 여기에 로딩 체크를 추가할 수 있습니다.
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val optionIdLong = selectedOptionId.toLongOrNull() ?: 0L
            val battleIdLong = battleId.toLongOrNull() ?: 0L

            Log.d(
                TAG,
                "[FLOW] 투표 전송 시작. Type: $voteType, Battle ID: $battleIdLong, Option ID: $optionIdLong"
            )

            voteUseCases.submitVoteUseCase(
                battleIdLong,
                optionIdLong,
                isPreVote = voteType == VoteType.PRE
            )
                .onSuccess { result ->
                    when (result) {
                        is SubmitVoteResult.Success -> {
                            Log.i(TAG, "[NAV] 투표 전송 성공")

                            if (voteType == VoteType.PRE) {
                                analyticsTracker.trackBattleStep(
                                    stepName = BattleStepName.PRE_VOTE,
                                    contentId = battleId,
                                    choice = selectedOptionId
                                )
                                Log.d(TAG, "[STATE] battle_step(pre_vote) 믹스패널 이벤트 전송 완료")
                            } else {
                                // is_changed: 투표 이력 조회로 사전/사후 선택 변경 여부 확인 (실패 시 미첨부)
                                val isChanged = voteUseCases.getMyVoteHistoryUseCase(battleIdLong)
                                    .getOrNull()?.opinionChanged
                                analyticsTracker.trackBattleStep(
                                    stepName = BattleStepName.POST_VOTE,
                                    contentId = battleId,
                                    choice = selectedOptionId,
                                    isChanged = isChanged
                                )
                                Log.d(
                                    TAG,
                                    "[STATE] battle_step(post_vote) 믹스패널 이벤트 전송 완료 (is_changed: $isChanged)"
                                )
                            }

                            // 성공 상태 업데이트 및 콜백
                            _uiState.update { it.copy(isLoading = false) }
                            onSuccess()
                        }

                        is SubmitVoteResult.InsufficientPoints -> {
                            Log.i(TAG, "[STATE] 포인트 부족 에러 감지 -> 충전 다이얼로그 노출")
                            _uiState.update {
                                it.copy(
                                    isInsufficientPoints = true,
                                    isLoading = false
                                )
                            }
                        }
                    }
                }
                .onFailure { error ->
                    Log.w(TAG, "[FLOW] 투표 전송 실패: ${error.message}")
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
        }
    }

    fun getShareLink(
        battleId: Int,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            Log.d(TAG, "[FLOW] 공유 링크 생성 요청. Battle ID: $battleId")
            shareUseCases.getBattleShareLinkUseCase(battleId)
                .onSuccess { shareUrl ->
                    Log.i(TAG, "[STATE] 공유 링크 생성 성공")
                    onSuccess(shareUrl.shareUrl)
                }
                .onFailure { error ->
                    Log.w(TAG, "[FLOW] 공유 링크 생성 실패: ${error.message}")
                    onError(error.message ?: "링크를 불러오는데 실패했습니다.")
                }
        }
    }

    fun dismissPointDialog() {
        Log.d(TAG, "[STATE] 포인트 부족 다이얼로그 닫기")
        _uiState.update { it.copy(isInsufficientPoints = false) }
    }

    /** 배틀 공유 시도 시 호출 (share_action target=battle) */
    fun trackShare(channel: String) {
        analyticsTracker.trackShareAction(ShareTarget.BATTLE, channel)
    }
}