package com.swyp4.team2.ui.debate

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.AudioPlayerBar
import com.swyp4.team2.ui.component.ChatBubble
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun DebateScreen(
    viewModel: DebateViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    // 구독한 uiState 바구니에서 필요한 데이터들을 깔끔하게 꺼내 씁니다.
    val scripts = uiState.scripts
    val activeIndex = uiState.activeIndex
    val isPlaying = uiState.isPlaying
    val currentPositionMs = uiState.currentPositionMs
    val totalDurationMs = uiState.totalDurationMs
    val interactiveOptions = uiState.interactiveOptions

    val listState = rememberLazyListState()

    // 활성화된 말풍선 인덱스가 바뀔 때마다 스크롤 이동
    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0 && activeIndex < scripts.size){
            listState.animateScrollToItem(index = activeIndex)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = SwypTheme.colors.surface,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "뒤샹의 변기, 예술인가 도발인가",
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.surface,
                    actions = {
                        IconButton(onClick = { /* 다시 듣기 로직 */ }) {
                            Icon(
                                painterResource(R.drawable.ic_reload),
                                contentDescription = "다시듣기",
                                tint = Gray900
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            AudioPlayerBar(
                isPlaying = isPlaying,
                currentPositionMs = currentPositionMs.toLong(),
                totalDurationMs = totalDurationMs.toLong(),
                onPlayPauseClick = { viewModel.togglePlayPause() },
                onSeek = { ratio -> viewModel.seekToPosition(ratio) },
                onRewindClick = { viewModel.seekRewind() },
                onForwardClick = { viewModel.seekForward() },
            )
        },
    ){ innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            itemsIndexed(scripts) { index, message ->
                val isFirstInGroup = index == 0 || scripts[index - 1].speakerType != message.speakerType

                // 다른 사람이 말하기 시작할 때 간격을 살짝 띄워줍니다
                if (isFirstInGroup && index > 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                ChatBubble(
                    message = message,
                    isActive = index == activeIndex,
                    showAvatarAndName = isFirstInGroup
                )
            }

            // 🌟 대화가 끝나고 선택지가 있다면 하단에 렌더링합니다!
            if (interactiveOptions.isNotEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp, bottom = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 구분선 및 안내 문구
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

                        interactiveOptions.forEach { option ->
                            OptionSelectionButton(
                                text = option.text, // 백엔드에서 받아온 선택지 텍스트
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

// 선택지 버튼 전용 컴포넌트
@Composable
fun OptionSelectionButton(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(4.dp))
            .border(1.dp, Color(0xFFCBA572), RoundedCornerShape(4.dp)) // 황금색 테두리
            .background(Color(0xFFF9F6F0)) // 옅은 베이지 배경
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