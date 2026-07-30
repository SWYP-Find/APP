package com.picke.presentation.ui.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.SwypTheme

// 로딩 스켈레톤에 좌->우로 흐르는 반짝임 효과를 입히는 Modifier.
// 뷰의 실제 크기(size)를 알아야 그라데이션이 뷰 폭에 맞게 흐르므로, onGloballyPositioned로 크기를 잰다.
// 밝은 배경 화면은 기본값(surfaceTertiary/surfaceDefault)을 쓰고, 검은 배경 같은 화면은
// baseColor/highlightColor를 어두운 톤으로 넘겨서 쓰면 된다.
fun Modifier.shimmer(
    baseColor: Color? = null,
    highlightColor: Color? = null
): Modifier = composed {
    var size by remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -2f * size.width,
        targetValue = 2f * size.width,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslate"
    )
    val resolvedBase = baseColor ?: SwypTheme.colors.surfaceTertiary
    val resolvedHighlight = highlightColor ?: SwypTheme.colors.surfaceDefault

    this
        .background(
            brush = Brush.linearGradient(
                colors = listOf(resolvedBase, resolvedHighlight, resolvedBase),
                start = Offset(translateAnim, 0f),
                end = Offset(translateAnim + size.width, size.height.toFloat())
            )
        )
        .onGloballyPositioned { size = it.size }
}

// 스켈레톤에서 텍스트 한 줄 자리를 표시할 때 쓰는 공용 조각.
@Composable
fun SkeletonLine(
    width: Dp,
    height: Dp,
    modifier: Modifier = Modifier,
    baseColor: Color? = null,
    highlightColor: Color? = null
) {
    Spacer(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(4.dp))
            .shimmer(baseColor, highlightColor)
    )
}
