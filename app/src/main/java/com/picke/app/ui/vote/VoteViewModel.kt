package com.picke.app.ui.vote

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.picke.app.data.local.TokenManager
import com.picke.app.di.AdMobManager
import com.picke.app.domain.model.BattleDetailBoard
import com.picke.app.domain.repository.BattleRepository
import com.picke.app.domain.repository.ShareRepository
import com.picke.app.domain.repository.VoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

enum class VoteType {
    PRE,  // 사전 투표 (밝은 테마)
    POST  // 사후 투표 (어두운 테마)
}

data class VoteUiState(
    val isLoading: Boolean = false, // 로딩 상태
    val battleDetail: BattleDetailBoard? = null, // 배틀 상세 정보
    val error: String? = null, // 에러 메시지
    val isInsufficientPoints: Boolean = false // 포인트 부족 여부 (다이얼로그 트리거)
)

@HiltViewModel
class VoteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val battleRepository: BattleRepository,
    private val voteRepository: VoteRepository,
    private val shareRepository: ShareRepository,
    private val tokenManager: TokenManager,
    val adMobManager: AdMobManager,
    private val mixpanel: MixpanelAPI
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
        tokenManager.getUserTag()?.let { tag ->
            Log.d(TAG, "[FLOW] 광고 리로드 시작. UserTag: $tag")
            adMobManager.loadAd(tag)
        } ?: Log.w(TAG, "[FLOW] UserTag 없음: 광고 리로드 스킵")
    }

    private fun fetchVoteDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val battleIdLong = battleId.toLongOrNull() ?: 0L
            Log.d(TAG, "[FLOW] 배틀 상세 정보 호출 시작. Battle ID: $battleIdLong")

            battleRepository.getBattleDetail(battleIdLong)
                .onSuccess { detailBoard ->
                    Log.i(TAG, "[STATE] 배틀 상세 정보 로드 성공")
                    _uiState.update {
                        it.copy(isLoading = false, battleDetail = detailBoard, error = null)
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

            Log.d(TAG, "[FLOW] 투표 전송 시작. Type: $voteType, Battle ID: $battleIdLong, Option ID: $optionIdLong")

            val result = if (voteType == VoteType.PRE) {
                voteRepository.submitPreVote(battleIdLong, optionIdLong)
            } else {
                voteRepository.submitPostVote(battleIdLong, optionIdLong)
            }

            result.onSuccess {
                Log.i(TAG, "[NAV] 투표 전송 성공")

                try {
                    val props = JSONObject().apply {
                        put("battle_id", battleIdLong)
                        put("selected_option_id", optionIdLong)
                    }

                    if (voteType == VoteType.PRE) {
                        mixpanel.track("pre_vote", props)
                        Log.d(TAG, "[STATE] pre_vote 믹스패널 이벤트 전송 완료")
                    } else {
                        mixpanel.track("post_vote", props)
                        Log.d(TAG, "[STATE] post_vote 믹스패널 이벤트 전송 완료")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "[FLOW] 믹스패널 이벤트 전송 실패: ${e.message}")
                }

                // 성공 상태 업데이트 및 콜백
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()

            }.onFailure { error ->
                Log.w(TAG, "[FLOW] 투표 전송 실패: ${error.message}")

                if (error.message?.contains("CREDIT_400_INSUFFICIENT") == true) {
                    Log.i(TAG, "[STATE] 포인트 부족 에러 감지 -> 충전 다이얼로그 노출")
                    _uiState.update { it.copy(isInsufficientPoints = true, isLoading = false) }
                } else {
                    _uiState.update { it.copy(error = error.message, isLoading = false) }
                }
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
            shareRepository.getBattleShareLink(battleId)
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
}