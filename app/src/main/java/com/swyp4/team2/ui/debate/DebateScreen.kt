package com.swyp4.team2.ui.debate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.ui.component.AudioPlayerBar
import com.swyp4.team2.ui.component.ChatBubble

@Composable
fun DebateScreen(
    viewModel: DebateViewModel = hiltViewModel()
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
        }
    ){ innerPadding ->
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            itemsIndexed(scripts) {index, message->
                ChatBubble(
                    message = message,
                    isActive = index == activeIndex
                )
            }
        }
    }

}