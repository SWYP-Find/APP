package com.picke.presentation.ui.scenario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.picke.presentation.ui.component.AudioPlayerBar
import com.picke.presentation.ui.component.ChatBubble
import com.picke.presentation.ui.component.CustomConfirmDialog
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.scenario.model.ScenarioOptionUiModel
import com.picke.presentation.ui.theme.PickeTheme
import kotlinx.coroutines.delay

@Composable
fun ScenarioScreen(
    battleId: String,
    viewModel: ScenarioViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onNextClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    LaunchedEffect(battleId) {
        viewModel.loadScenario(battleId)
    }

    val visibleScripts = if (uiState.maxRevealedIndex >= 0) {
        val safeIndex = minOf(uiState.maxRevealedIndex + 1, uiState.scripts.size)
        uiState.scripts.subList(0, safeIndex)
    } else emptyList()

    val allDisplayScripts = uiState.pastScripts + visibleScripts

    LaunchedEffect(uiState.showOptions) {
        if (uiState.showOptions) {
            delay(100)
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
                onPlayPauseClick = { viewModel.togglePlayPause() },
                onSeek = { ratio -> viewModel.seekToPosition(ratio) },
                onRewindClick = { viewModel.seekRewind() },
                onForwardClick = { viewModel.seekForward() },
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
                val isNewSpeaker = index == 0 || allDisplayScripts[index - 1].speakerType != script.speakerType
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
                            val targetRatio = script.startTimeMs.toFloat() / uiState.totalDurationMs.toFloat()
                            viewModel.seekToPosition(targetRatio)
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
                        onOptionClick = { viewModel.selectOption(it) }
                    )
                }
            }
            if (uiState.isReviewing) {
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    OptionConfirmButton(
                        text = "최종투표 하러가기",
                        isEnabled = true,
                        onClick = { onNextClick() }
                    )
                }
            }

        }

        if (uiState.showFinalVoteDialog) {
            CustomConfirmDialog(
                message = "최종투표하고 투표 결과를 확인하시겠습니까?",
                dismissText = "최종투표하기",
                confirmText= "다시 들어볼래요",
                 onDismiss= {
                    viewModel.dismissFinalDialog()
                    onNextClick()
                 },
                 onConfirm= {
                    viewModel.restartCurrentAudio()
                }
            )
        }
    }
}

@Composable
fun InteractiveOptionsUI(
    options: List<ScenarioOptionUiModel>,
    selectedNodeId: String?,
    onOptionClick: (String) -> Unit
) {
    var pendingSelectedId by remember(options) {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 32.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            HorizontalDivider(modifier = Modifier.weight(1f), color = PickeTheme.colors.neutral200)
            Text(
                text = if (selectedNodeId == null) "이제 당신의 입장을 선택해주세요" else "아래가 당신의 선택입니다.",
                style = PickeTheme.typography.labelMedium.copy(fontStyle = FontStyle.Italic),
                color = PickeTheme.colors.textTertiary,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            HorizontalDivider(modifier = Modifier.weight(1f), color = PickeTheme.colors.neutral200)
        }

        Spacer(modifier = Modifier.height(24.dp))

        options.forEach { option ->
            val isCommitted = selectedNodeId == option.nextNodeId
            val isPending = pendingSelectedId == option.nextNodeId
            val isSelected = isCommitted || isPending

            OptionSelectionCard(
                text = option.label,
                isSelected = isSelected,
                isEnabled = selectedNodeId == null,
                onClick = {
                    if (selectedNodeId == null) {
                        pendingSelectedId = option.nextNodeId
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        if (selectedNodeId == null) {
            OptionConfirmButton(
                text = "입장 선택하기",
                isEnabled = pendingSelectedId != null,
                onClick = {
                    pendingSelectedId?.let { onOptionClick(it) }
                }
            )
        }
    }
}

@Composable
fun OptionSelectionCard(
    text: String,
    isSelected: Boolean,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) PickeTheme.colors.secondary else PickeTheme.colors.borderSubtle
    val bgColor = if (isSelected) PickeTheme.colors.surfaceTertiary else PickeTheme.colors.surfaceTertiary

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(2.dp))
            .clickable(enabled = isEnabled) { onClick() }
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = PickeTheme.typography.b5Medium,
            color = if (isSelected) PickeTheme.colors.textPrimary else PickeTheme.colors.textTertiary,
            textAlign = TextAlign.Center
        )
    }
}

// 하단 최종 확정 버튼 컴포저블
@Composable
fun OptionConfirmButton(
    text: String,
    isEnabled: Boolean,
    onClick: () -> Unit
) {
    val bgColor = if (isEnabled) PickeTheme.colors.buttonPrimaryBackground else PickeTheme.colors.buttonPrimaryBackgroundDisabled
    val textColor = PickeTheme.colors.buttonPrimaryText

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(bgColor)
            .clickable(enabled = isEnabled) { onClick() }
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = PickeTheme.typography.b5Medium,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}