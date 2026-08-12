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
                            .background(
                                color = PickeTheme.colors.primary,
                                shape = CircleShape
                            )
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
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(trackHeight)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFE0E0E0))
                    )
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ControlSkipButton(
                iconResId = R.drawable.ic_play_back,
                label = "15초",
                onClick = onRewindClick
            )

            Spacer(modifier = Modifier.width(20.dp))
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
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(20.dp))
            ControlSkipButton(
                iconResId = R.drawable.ic_play_forward,
                label = "15초",
                onClick = onForwardClick
            )
        }
    }
}

@Composable
private fun ControlSkipButton(
    iconResId: Int,
    label: String,
    onClick: () -> Unit
) {
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