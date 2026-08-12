package com.picke.presentation.ui.battleentry

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

    LaunchedEffect(routeEvent) {
        when (routeEvent) {
            "PRE_VOTE" -> onNavigateToPreVote(viewModel.battleId)
            "PERSPECTIVE" -> onNavigateToPerspective(viewModel.battleId)
        }
    }

    BattleEntrySkeleton(modifier = Modifier.fillMaxSize())
}
