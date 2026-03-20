package com.swyp4.team2.ui.debate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.AudioPlayerBar
import com.swyp4.team2.ui.component.ChatBubble
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun DebateScreen(
    viewModel: DebateViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
) {
    val scripts by viewModel.scripts.collectAsState()
    val activeIndex by viewModel.activeIndex.collectAsState()

    // 플레이어 상태들 구독
    val isPlaying by viewModel.isPlaying.collectAsState()
    val currentPositionMs by viewModel.currentPositionMs.collectAsState()
    val totalDurationMs by viewModel.totalDurationMs.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(activeIndex) {
        if (activeIndex >= 0 && activeIndex < scripts.size){
            listState.animateScrollToItem(index = activeIndex)
        }
    }

    Scaffold(
        containerColor = SwypTheme.colors.surface,
        topBar = {
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
        },
        bottomBar = {
            AudioPlayerBar(
                isPlaying = isPlaying,
                currentPositionMs = currentPositionMs,
                totalDurationMs = totalDurationMs,
                onPlayPauseClick = { viewModel.togglePlayPause() },
                onSeek = { ratio -> viewModel.seekToPosition(ratio) },
                onRewindClick = { viewModel.seekRewind() },
                onForwardClick = { viewModel.seekForward() },
                onSpeedClick = { /* 배속 기능은 나중에 추가! */ }
            )
        },
    ){ innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
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
        }
    }

}