package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AudioPlayerBar(
    isPlaying: Boolean,
    currentPositionMs: Long,
    totalDurationMs: Long,
    onPlayPauseClick: () -> Unit, // 멈췄을때
    onSeek: (Float) -> Unit,     // 슬라이더를 눌러서 다른 시간대로 드래그 했을때
    onRewindClick: () -> Unit,   // 15초 뒤로 가기
    onForwardClick: () -> Unit,  // 15초 앞으로 가기
    onSpeedClick: () -> Unit,    // 배속 변경하기
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ){
            // 현재 재생 시점
            Text(
                text = formatTime(currentPositionMs),
                fontSize = 12.sp,
                color = Color.Gray
            )
            // 슬라이더
            Slider(
                value = if (totalDurationMs > 0) currentPositionMs.toFloat() / totalDurationMs else 0f,
                onValueChange = onSeek,
                modifier = Modifier.weight(1f)
                    .padding(horizontal = 8.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Black,
                    activeTrackColor = Color.Black,
                    inactiveTrackColor = Color.LightGray,
                )
            )
            // 총 재생 시간
            Text(
                text = formatTime(totalDurationMs),
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            // 1.0x 배속 버튼
            Box(
                modifier = Modifier.clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray)
                    .clickable{onSpeedClick()}
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "1.0x",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // 15초 뒤로 가기
            IconButton(onClick = { onRewindClick }){
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "15초 뒤로",
                    tint = Color.Gray
                )
            }

            // 재생/일시정지 메인 버튼
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF222222))
                    .clickable { onPlayPauseClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Clear else Icons.Default.PlayArrow,
                    contentDescription = "재생/일시정지",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }

            // 15초 앞으로 가기
            IconButton(onClick = { onForwardClick }){
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "15초 앞으로",
                    tint = Color.Gray
                )
            }

            Box(modifier = Modifier.width(48.dp))
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}