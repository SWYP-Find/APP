package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.swyp4.team2.R
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.SwypTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AudioPlayerBar(
    isPlaying: Boolean,
    currentPositionMs: Long,
    totalDurationMs: Long,
    onPlayPauseClick: () -> Unit,
    onSeek: (Float) -> Unit,
    onRewindClick: () -> Unit,
    onForwardClick: () -> Unit,
    onSpeedClick: () -> Unit, // (UI에선 안 쓰지만 파라미터 유지를 위해 둠)
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding() // 하단 안전영역 확보
            .padding(horizontal = 20.dp, vertical = 24.dp) // 전체 여백 넉넉하게
    ) {
        // 1. 슬라이더 (맨 위에 길게)
        Slider(
            value = if (totalDurationMs > 0) currentPositionMs.toFloat() / totalDurationMs else 0f,
            onValueChange = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp), // 슬라이더 터치 영역 조절
            colors = SliderDefaults.colors(
                thumbColor = SwypTheme.colors.primary,          // 동그라미 색상
                activeTrackColor = SwypTheme.colors.primary,    // 지나간 선 색상
                inactiveTrackColor = Gray400        // 안 지나간 선 색상
            ),
            // 동그라미(Thumb) 크기를 피그마처럼 작게 만들기 위한 커스텀 설정
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    colors = SliderDefaults.colors(thumbColor = SwypTheme.colors.primary),
                    thumbSize = DpSize(12.dp, 12.dp)
                )
            }
        )

        Spacer(modifier = Modifier.height(4.dp))

        // 2. 시간 정보 (양 끝 정렬)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPositionMs),
                style = SwypTheme.typography.labelXSmall,
                color = Gray400
            )
            Text(
                text = formatTime(totalDurationMs),
                style = SwypTheme.typography.labelXSmall,
                color = Gray400
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. 컨트롤 버튼 영역 (중앙 정렬)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 15초 뒤로 가기
            IconButton(
                onClick = onRewindClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play_back),
                    contentDescription = "15초 뒤로",
                    tint = SwypTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(32.dp)) // 버튼 사이 간격

            // 재생/일시정지 메인 버튼
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    // 배경색 없이 아이콘 자체만 크게!
                    .clickable { onPlayPauseClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = if (isPlaying) painterResource(R.drawable.ic_play)
                    else painterResource(R.drawable.ic_play),
                    contentDescription = "재생/일시정지",
                    tint = SwypTheme.colors.primary, // 갈색 적용
                    modifier = Modifier.size(48.dp) // 피그마처럼 크게
                )
            }

            Spacer(modifier = Modifier.width(32.dp))

            // 15초 앞으로 가기
            IconButton(
                onClick = onForwardClick,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_play_foward),
                    contentDescription = "15초 앞으로",
                    tint = SwypTheme.colors.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return String.format("%d:%02d", minutes, seconds)
}