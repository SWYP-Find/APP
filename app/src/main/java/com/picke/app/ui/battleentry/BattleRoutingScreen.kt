package com.picke.app.ui.battleentry

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

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
    // 사전투표/관점 중 어디로 갈지 아직 모르는 상태이므로, 특정 화면의 모양을
    // 흉내내지 않는 중립적인 shimmer만 보여준다.
    BattleEntrySkeleton(modifier = Modifier.fillMaxSize())
}
