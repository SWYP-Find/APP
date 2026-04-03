package com.picke.app.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ChattingLoadingAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars_5")
    val baseHeights = listOf(6.dp, 10.dp, 15.dp, 10.dp, 6.dp)
    val maxScales = listOf(1.1f, 1.3f, 1.6f, 1.3f, 1.1f)

    val animations = (0 until 5).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = maxScales[index],
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(offsetMillis = index * 120)
            ),
            label = "scale_${index + 1}"
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        repeat(5) { index ->
            Box(
                modifier = Modifier
                    .size(width = 2.5.dp, height = baseHeights[index])
                    .scale(scaleX = 1f, scaleY = animations[index].value)
                    .background(Color(0xFF8D4B38), RoundedCornerShape(50))
            )
        }
    }
}