package com.picke.presentation.ui.scenario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.picke.presentation.ui.component.CustomConfirmDialog
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.scenario.component.AudioPlayerBar
import com.picke.presentation.ui.scenario.component.ChatBubble
import com.picke.presentation.ui.scenario.component.InteractiveOptionsUI
import com.picke.presentation.ui.scenario.component.OptionConfirmButton
import com.picke.presentation.ui.scenario.model.ScenarioUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun ScenarioScreen(
    battleId: String,
    viewModel: ScenarioViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(battleId) {
        viewModel.loadScenario(battleId)
    }

    ScenarioScreen(
        uiState = uiState,
        onPlayPauseClick = { viewModel.togglePlayPause() },
        onSeek = { ratio -> viewModel.seekToPosition(ratio) },
        onRewindClick = { viewModel.seekRewind() },
        onForwardClick = { viewModel.seekForward() },
        onSeekToPosition = { targetRatio -> viewModel.seekToPosition(targetRatio) },
        onSelectOption = { option -> viewModel.selectOption(option) },
        onDismissFinalDialog = { viewModel.dismissFinalDialog() },
        onRestartCurrentAudio = { viewModel.restartCurrentAudio() },
        onNextClick = onNextClick
    )
}

@Composable
fun ScenarioScreen(
    uiState: ScenarioUiState,
    onPlayPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
    onRewindClick: () -> Unit,
    onForwardClick: () -> Unit,
    onSeekToPosition: (Float) -> Unit,
    onSelectOption: (String) -> Unit,
    onDismissFinalDialog: () -> Unit,
    onRestartCurrentAudio: () -> Unit,
    onNextClick: () -> Unit,
) {
    val listState = rememberLazyListState()

    val visibleScripts = if (uiState.maxRevealedIndex >= 0) {
        val safeIndex = minOf(uiState.maxRevealedIndex + 1, uiState.scripts.size)
        uiState.scripts.subList(0, safeIndex)
    } else emptyList()

    val allDisplayScripts = uiState.pastScripts + visibleScripts

    LaunchedEffect(uiState.showOptions) {
        if (uiState.showOptions) {
            delay(100.milliseconds)
            listState.animateScrollToItem(allDisplayScripts.size)
        }
    }

    LaunchedEffect(uiState.activeIndex, uiState.pastScripts.size, uiState.showOptions) {
        if (uiState.showOptions) return@LaunchedEffect

        val targetIndex = uiState.pastScripts.size + uiState.activeIndex
        if (targetIndex >= 0 && targetIndex < allDisplayScripts.size) {
            listState.animateScrollToItem(index = targetIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = PickeTheme.colors.backgroundBrand,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = uiState.title,
                    centerTitle = true,
                    showBackButton = false,
                    backgroundColor = PickeTheme.colors.backgroundBrand,
                )
            }
        },
        bottomBar = {
            AudioPlayerBar(
                isPlaying = uiState.isPlaying,
                currentPositionMs = uiState.currentPositionMs,
                totalDurationMs = uiState.totalDurationMs,
                onPlayPauseClick = onPlayPauseClick,
                onSeek = onSeek,
                onRewindClick = onRewindClick,
                onForwardClick = onForwardClick,
            )
        }
    ) { innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(top = 24.dp, bottom = 40.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            itemsIndexed(allDisplayScripts) { index, script ->
                val isNewSpeaker =
                    index == 0 || allDisplayScripts[index - 1].speakerType != script.speakerType
                val isNewNode = uiState.pastChoices.any { it.scriptIndex == index - 1 }
                val showAvatarAndName = isNewSpeaker || isNewNode

                val isPastScript = index < uiState.pastScripts.size
                val currentAudioScriptIndex = index - uiState.pastScripts.size

                if (showAvatarAndName && index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                ChatBubble(
                    script = script,
                    isActive = !isPastScript && currentAudioScriptIndex == uiState.activeIndex && uiState.isPlaying,
                    showAvatarAndName = showAvatarAndName,
                    onClick = {
                        if (!isPastScript && uiState.totalDurationMs > 0) {
                            val targetRatio =
                                script.startTimeMs.toFloat() / uiState.totalDurationMs.toFloat()
                            onSeekToPosition(targetRatio)
                        }
                    }
                )

                val pastChoice = uiState.pastChoices.find { it.scriptIndex == index }
                if (pastChoice != null) {
                    InteractiveOptionsUI(
                        options = pastChoice.options,
                        selectedNodeId = pastChoice.selectedNextNodeId,
                        onOptionClick = {}
                    )
                }
            }

            if (uiState.interactiveOptions.isNotEmpty() && uiState.showOptions) {
                item {
                    InteractiveOptionsUI(
                        options = uiState.interactiveOptions,
                        selectedNodeId = null,
                        onOptionClick = onSelectOption
                    )
                }
            }
            if (uiState.isReviewing) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    OptionConfirmButton(
                        text = "최종투표 하러가기",
                        isEnabled = true,
                        onClick = onNextClick
                    )
                }
            }

        }

        if (uiState.showFinalVoteDialog) {
            CustomConfirmDialog(
                message = "최종투표하고 투표 결과를 확인하시겠습니까?",
                dismissText = "최종투표하기",
                confirmText = "다시 들어볼래요",
                onDismiss = {
                    onDismissFinalDialog()
                    onNextClick()
                },
                onConfirm = onRestartCurrentAudio
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ScenarioScreenPreview() {
    PickeTheme {
        ScenarioScreen(
            uiState = ScenarioUiState(
                pastScripts = DummyData.dummyScripts,
                pastChoices = DummyData.dummyPastChoices,
                scripts = DummyData.dummyScripts,
                interactiveOptions = DummyData.dummyPastChoices.first().options,
            ),
            onPlayPauseClick = {},
            onSeek = {},
            onRewindClick = {},
            onForwardClick = {},
            onSeekToPosition = {},
            onSelectOption = {},
            onDismissFinalDialog = {},
            onRestartCurrentAudio = {},
            onNextClick = {}
        )
    }
}