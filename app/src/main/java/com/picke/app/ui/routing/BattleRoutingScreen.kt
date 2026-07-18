package com.picke.app.ui.routing

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.app.ui.vote.VoteSkeleton
import com.picke.app.ui.vote.VoteType

@Composable
fun BattleRoutingScreen(
    onNavigateToPreVote: (String) -> Unit,
    onNavigateToPerspective: (String) -> Unit,
    viewModel: BattleRoutingViewModel = hiltViewModel()
) {
    val routeEvent = viewModel.routeEvent.collectAsStateWithLifecycle().value

    // 상태값이 바뀌면 즉시 화면 이동 (라우팅 화면은 백스택에서 지워지도록 AppNavigation에서 처리)
    LaunchedEffect(routeEvent) {
        when (routeEvent) {
            "PRE_VOTE" -> onNavigateToPreVote(viewModel.battleId)
            "PERSPECTIVE" -> onNavigateToPerspective(viewModel.battleId)
        }
    }

    // API 결과를 기다리는 아주 짧은 시간 동안 보여줄 로딩 화면.
    // 목적지 대부분(NONE/PRE_VOTE/SCENARIO/실패 시 기본값)이 사전투표 화면이므로
    // 그 화면의 스켈레톤을 미리 보여줘서 전환이 매끄럽게 이어지도록 한다.
    VoteSkeleton(voteType = VoteType.PRE, modifier = Modifier.fillMaxSize())
}
