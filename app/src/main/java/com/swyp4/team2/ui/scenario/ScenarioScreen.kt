import android.os.Build
import android.view.WindowInsets
import androidx.annotation.RequiresExtension
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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.AudioPlayerBar
import com.swyp4.team2.ui.component.ChatBubble
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.scenario.ScenarioViewModel
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige700
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary500
import com.swyp4.team2.ui.theme.SwypTheme

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
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

    LaunchedEffect(uiState.activeIndex, uiState.pastScripts.size) {
        val targetIndex = uiState.pastScripts.size + uiState.activeIndex
        if (targetIndex >= 0 && targetIndex < allDisplayScripts.size) {
            listState.animateScrollToItem(index = targetIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SwypTheme.colors.surface,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = uiState.title,
                    showBackButton = false,
                    backgroundColor = SwypTheme.colors.surface,
                    actions = {
                        IconButton(onClick = { viewModel.loadScenario(battleId) }) {
                            Icon(painterResource(R.drawable.ic_reload), contentDescription = "다시듣기", tint = Gray900)
                        }
                    }
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
                val isFirstInGroup = index == 0 || allDisplayScripts[index - 1].speakerType != script.speakerType
                val isPastScript = index < uiState.pastScripts.size
                val currentAudioScriptIndex = index - uiState.pastScripts.size

                if (isFirstInGroup && index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                ChatBubble(
                    script = script,
                    isActive = !isPastScript && currentAudioScriptIndex == uiState.activeIndex && uiState.isPlaying,
                    showAvatarAndName = isFirstInGroup,
                    onClick = {
                        if (!isPastScript && uiState.totalDurationMs > 0) {
                            val targetRatio = script.startTimeMs.toFloat() / uiState.totalDurationMs.toFloat()
                            viewModel.seekToPosition(targetRatio)
                        }
                    }
                )
            }

            if (uiState.interactiveOptions.isNotEmpty() && uiState.showOptions) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Gray200)
                            Text(
                                text = "이제 당신의 입장을 선택해주세요",
                                style = SwypTheme.typography.labelMedium.copy(fontStyle = FontStyle.Italic),
                                color = Gray500,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Gray200)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        uiState.interactiveOptions.forEachIndexed { index, option ->
                            OptionSelectionButton(
                                text = option.label,
                                isPrimary = index == 0,
                                onClick = { viewModel.selectOption(option.nextNodeId) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OptionSelectionButton(
    text: String,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    val borderColor = if (isPrimary) Secondary500 else Beige700

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .background(Beige400)
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SwypTheme.typography.b5Medium,
            color = Gray900,
            textAlign = TextAlign.Center
        )
    }
}