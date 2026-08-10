package com.picke.presentation.ui.scenario.component

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

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
        // 1. 슬라이더
        // Material3의 SliderDefaults.Track은 내부 디자인 토큰 때문에 height 모디파이어를
        // 줘도 실제로는 그보다 두껍게 그려져서, 트랙을 직접 두 개의 얇은 막대(Box)로 그린다.
        // thumb/track 모두 슬라이더와 동일한 높이(20dp) 박스로 감싸 중앙 정렬해서
        // 두 요소의 중심이 항상 일치하도록 맞춘다.
        val sliderHeight = 20.dp
        val trackHeight = 2.dp
        Slider(
            value = if (totalDurationMs > 0) currentPositionMs.toFloat() / totalDurationMs else 0f,
            onValueChange = onSeek,
            modifier = Modifier
                .fillMaxWidth()
                .height(sliderHeight),
            thumb = {
                Box(
                    modifier = Modifier.height(sliderHeight),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .background(color = PickeTheme.colors.primary, shape = CircleShape)
                    )
                }
            },
            track = { sliderState ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sliderHeight),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // 비활성 트랙 (전체 길이)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(trackHeight)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFE0E0E0))
                    )
                    // 활성 트랙 (재생된 만큼)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(fraction = sliderState.value.coerceIn(0f, 1f))
                            .height(trackHeight)
                            .clip(RoundedCornerShape(50))
                            .background(PickeTheme.colors.primary)
                    )
                }
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
                style = PickeTheme.typography.labelXSmall,
                color = PickeTheme.colors.textTertiary
            )
            Text(
                text = formatTime(totalDurationMs),
                style = PickeTheme.typography.labelXSmall,
                color = PickeTheme.colors.textTertiary
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
                        if (isPlaying) R.drawable.ic_play_stop else R.drawable.ic_play
                    ),
                    contentDescription = null,
                    tint = PickeTheme.colors.primaryDarkest,
                    modifier = Modifier.size(36.dp) // 아이콘 자체 크기 키움
                )
            }

            Spacer(modifier = Modifier.width(20.dp))

            // 15초 앞으로 가기
            ControlSkipButton(
                iconResId = R.drawable.ic_play_forward,
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
            tint = PickeTheme.colors.primaryDarkest,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = PickeTheme.typography.labelXSmall,
            color = PickeTheme.colors.textTertiary
        )
    }
}

private fun formatTime(ms: Long): String {
    val totalSeconds = ms / 1000
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return "%d:%02d".format(minutes, seconds)
}

@Preview(showBackground = true)
@Composable
fun AudioPlayerBarPreview() {
    PickeTheme {
        AudioPlayerBar(
            isPlaying = true,
            currentPositionMs = 12,
            totalDurationMs = 40,
            onPlayPauseClick = { },
            onSeek = { },
            onRewindClick = { },
            onForwardClick = { }
        )
    }
}