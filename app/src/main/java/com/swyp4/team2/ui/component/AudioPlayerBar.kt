package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Primary900
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        // 1. 슬라이더 (트랙을 얇게, Thumb 패딩 제거)
        Slider(
            value = if (totalDurationMs > 0) currentPositionMs.toFloat() / totalDurationMs else 0f,
            onValueChange = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .height(20.dp),
            colors = SliderDefaults.colors(
                thumbColor = SwypTheme.colors.primary,
                activeTrackColor = SwypTheme.colors.primary,
                inactiveTrackColor = Gray300
            ),
            thumb = {
                Box(
                    modifier = Modifier
                        .size(12.dp)
                        .background(color = SwypTheme.colors.primary, shape = CircleShape)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    colors = SliderDefaults.colors(
                        activeTrackColor = SwypTheme.colors.primary,
                        inactiveTrackColor = Color(0xFFE0E0E0)
                    ),
                    modifier = Modifier.height(2.dp),
                    drawStopIndicator = {}
                )
            }
        )

        // 2. 시간 표시 영역
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = formatTime(currentPositionMs),
                style = SwypTheme.typography.labelXSmall,
                color = Gray500
            )
            Text(
                text = formatTime(totalDurationMs),
                style = SwypTheme.typography.labelXSmall,
                color = Gray500
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. 메인 컨트롤 영역
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 15초 뒤로 가기
            ControlSkipButton(
                iconResId = R.drawable.ic_play_back,
                label = "15초",
                onClick = onRewindClick
            )

            Spacer(modifier = Modifier.width(20.dp))

            // 재생/일시정지 버튼 (시안처럼 크게)
            IconButton(
                onClick = onPlayPauseClick,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    painter = painterResource(
                        if (isPlaying) R.drawable.ic_stop else R.drawable.ic_play
                    ),
                    contentDescription = null,
                    tint = Primary900,
                    modifier = Modifier.size(36.dp) // 아이콘 자체 크기 키움
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // 15초 앞으로 가기
            ControlSkipButton(
                iconResId = R.drawable.ic_play_foward,
                label = "15초",
                onClick = onForwardClick
            )
        }
    }
}

@Composable
private fun ControlSkipButton(iconResId: Int, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Icon(
            painter = painterResource(iconResId),
            contentDescription = label,
            tint = Primary900,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = SwypTheme.typography.labelXSmall,
            color = Gray500
        )
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}