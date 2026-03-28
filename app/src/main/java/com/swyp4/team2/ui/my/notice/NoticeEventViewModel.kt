package com.swyp4.team2.ui.my.notice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.swyp4.team2.domain.model.NoticeEventItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
data class NoticeEventUiState(
    val noticeList: List<NoticeEventItem> = emptyList(),
    val eventList: List<NoticeEventItem> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel
class NoticeEventViewModel @Inject constructor() : ViewModel(){
    private val _uiState = MutableStateFlow(NoticeEventUiState())
    val uiState: StateFlow<NoticeEventUiState> = _uiState.asStateFlow()

    init {
        fetchNoticeEvents()
    }
    private fun fetchNoticeEvents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            delay(1000)

            val dummyNotices = listOf(
                NoticeEventItem(
                    id = 1,
                    type = "공지사항",
                    title = "v1.2.0 업데이트 안내 (새로운 기능 추가!)",
                    date = "2026.03.28",
                    content = "안녕하세요, 사색하는 고양이님! 팀 SWYP입니다.\n\n이번 v1.2.0 업데이트에서는 많은 분들이 요청해주셨던 '토론 다시보기' 기능과 '유형별 통계' 데이터가 더욱 정교해졌습니다.\n\n[주요 업데이트 내용]\n1. 내 철학자 유형 상세 리포트 디자인 개편\n2. 배틀 참여 시 획득 포인트 상향 조정\n3. 앱 안정성 개선 및 버그 수정\n\n앞으로도 더 깊이 있는 사색의 공간을 만들기 위해 노력하겠습니다. 감사합니다!"
                ),
                NoticeEventItem(
                    id = 2,
                    type = "공지사항",
                    title = "서버 점검 사전 안내 (3/30 새벽 2시~4시)",
                    date = "2026.03.25",
                    content = "더욱 안정적인 서비스 제공을 위해 서버 점검이 진행될 예정입니다.\n\n점검 시간 동안은 앱 접속 및 모든 서비스 이용이 제한되오니 이용에 참고 부탁드립니다.\n\n■ 점검 일시: 2026년 3월 30일(월) 02:00 ~ 04:00 (약 2시간)\n■ 작업 내용: 데이터베이스 최적화 및 서버 보안 업데이트\n\n점검은 상황에 따라 조기 종료될 수 있으며, 소중한 데이터 보호를 위해 최선을 다하겠습니다. 불편을 드려 죄송합니다."
                ),
                NoticeEventItem(
                    id = 3,
                    type = "공지사항",
                    title = "개인정보 처리방침 변경 안내",
                    date = "2026.03.10",
                    content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.\n\n처음에는 말기 환자만을 위한 제도였지만, 시간이 지나면서 대상이 점점 확대되었고, '합리적 선택'이라는 이름 아래 사회적으로 취약한 사람들이 스스로 짐이 된다고 느끼는 상황이 만들어지고 있습니다.\n\n진정한 자율적 선택이란, 충분한 돌봄과 지원이 보장된 상태에서만 가능합니다. 경제적 부담, 가족에 대한 미안함, 사회적 시선 속에서 내리는 선택을 과연 '자유로운 선택'이라고 할 수 있을까요?"
                )
            )

            val dummyEvents = listOf(
                NoticeEventItem(
                    id = 4,
                    type = "이벤트",
                    title = "🎉 봄맞이 출석체크 이벤트! 포인트 2배 지급",
                    date = "2026.03.20",
                    content = "따뜻한 봄바람과 함께 찾아온 특별 혜택!\n\n이벤트 기간 동안 매일매일 앱에 접속만 해도 토론 참여에 필요한 포인트를 2배로 적립해 드립니다.\n\n■ 기간: 2026.03.20 ~ 2026.04.20\n■ 참여 방법: 앱 접속 시 자동 적립\n■ 혜택: 매일 10P -> 20P 지급!\n\n모아둔 포인트로 더 많은 배틀에 참여하고 나만의 철학자를 만나보세요!"
                ),
                NoticeEventItem(
                    id = 5,
                    type = "이벤트",
                    title = "첫 배틀 참여하고 특별 뱃지 받아가세요!",
                    date = "2026.03.01",
                    content = "아직 배틀에 참여하지 않으셨나요?\n\n지금 바로 오늘의 배틀에 참여하고 첫 번째 관점을 남겨주시는 모든 유저분들께 '사색의 시작' 한정판 프로필 뱃지를 증정합니다.\n\n■ 대상: 배틀 참여 0회 유저\n■ 선물: 전용 프로필 뱃지 및 50포인트 보너스\n\n여러분의 철학적인 첫걸음을 응원합니다. 지금 바로 참여해보세요!"
                )
            )

            _uiState.update {
                it.copy(
                    noticeList = dummyNotices,
                    eventList = dummyEvents,
                    isLoading = false
                )
            }
        }
    }
}