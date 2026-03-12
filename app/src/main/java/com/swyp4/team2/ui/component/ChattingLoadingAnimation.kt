package com.swyp4.team2.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChattingLoadingAnimation() {
    // 1. 무한히 반복되는 트랜지션(주문)을 만듭니다.
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars_5")

    // 🌟 2. 5개의 서로 다른 애니메이션 숫자(Scale)를 미리 정의합니다.
    // 각 애니메이션은 'delayMillis'가 서로 다르게 설정됩니다!
    val baseDuration = 600
    val baseEasing = LinearEasing
    val repeatMode = RepeatMode.Reverse

    val animations = (0 until 5).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.5f,
            targetValue = 1.5f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = baseDuration,
                    easing = baseEasing,
                    // 💥 막대기마다 딜레이를 다르게! (0, 120ms, 240ms, 360ms, 480ms)
                    delayMillis = index * (baseDuration / 5)
                ),
                repeatMode = repeatMode
            ),
            label = "scale_${index + 1}"
        )
    }
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        repeat(5) { index ->
            val variedHeight = (20 + index * 2).dp

            Box(
                modifier = Modifier
                    .size(width = 4.dp, height = variedHeight)
                    // 4. 각 막대기에 해당하는 미리 정의된 애니메이션 값을 세로 스케일로 적용합니다.
                    .scale(scaleX = 1f, scaleY = animations[index].value)
                    .background(Color(0xFF8D4B38), RoundedCornerShape(2.dp))
            )
        }
    }
}