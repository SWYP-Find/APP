import android.view.WindowInsets
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.AudioPlayerBar
import com.swyp4.team2.ui.component.ChatBubble
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.scenario.ScenarioViewModel
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary500
import com.swyp4.team2.ui.theme.SwypTheme

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
        viewModel.loadScenario("22222222-2222-2222-2222-000000000001")
    }

    val visibleScripts = if (uiState.activeIndex >= 0) {
        uiState.scripts.subList(0, uiState.activeIndex + 1)
    } else emptyList()

    // 활성화된 말풍선 인덱스가 바뀔 때마다 스크롤 부드럽게 이동
    LaunchedEffect(uiState.activeIndex) {
        if (uiState.activeIndex >= 0 && uiState.activeIndex < uiState.scripts.size) {
            listState.animateScrollToItem(index = uiState.activeIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SwypTheme.colors.surface,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = uiState.title,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.surface,
                    actions = {
                        IconButton(onClick = { onNextClick() }) {
                            Icon(painterResource(R.drawable.ic_reload), contentDescription = "다시듣기", tint = Gray900)
                        }
                    }
                )
            }
        },
        bottomBar = {
            // 하단 플레이어 바
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
            itemsIndexed(visibleScripts) { index, script ->
                val isFirstInGroup = index == 0 || visibleScripts[index - 1].speakerType != script.speakerType

                if (isFirstInGroup && index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                ChatBubble(
                    script = script,
                    isActive = index == visibleScripts.lastIndex && uiState.isPlaying,
                    showAvatarAndName = isFirstInGroup
                )
            }

            // 참여형 옵션 (A/B 선택지)
            if (uiState.interactiveOptions.isNotEmpty() && uiState.showOptions) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Gray500)
                            Text(
                                text = "이제 당신의 입장을 선택해주세요",
                                style = SwypTheme.typography.labelMedium,
                                color = Gray900,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            HorizontalDivider(modifier = Modifier.weight(1f), color = Gray500)
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        uiState.interactiveOptions.forEach { option ->
                            OptionSelectionButton(
                                text = option.label,
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
fun OptionSelectionButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, Secondary500, RoundedCornerShape(4.dp))
            .background(Beige400)
            .clickable { onClick() }
            .padding(vertical = 20.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SwypTheme.typography.b2Medium,
            color = Gray900,
            textAlign = TextAlign.Center
        )
    }
}
