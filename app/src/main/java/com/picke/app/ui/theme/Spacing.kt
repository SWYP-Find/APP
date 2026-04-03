package com.picke.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

// 1. Space Style (여백 시스템)
/*data class Spacing(
    val space01: Dp = 2.dp,
    val space02: Dp = 4.dp,
    val space03: Dp = 8.dp,
    val space04: Dp = 12.dp,
    val space05: Dp = 16.dp,
    val space06: Dp = 20.dp,
    val space07: Dp = 24.dp,
    val space08: Dp = 32.dp,
    val space09: Dp = 40.dp,
    val space10: Dp = 48.dp,
    val space11: Dp = 56.dp,
    val space12: Dp = 64.dp,
    val space13: Dp = 80.dp,
)*/

// 2. Elevation Style (깊이/그림자 시스템)
data class Elevation(
    val level1: Dp = 2.dp,
    val level2: Dp = 4.dp,
    val level3: Dp = 8.dp,
    val level4: Dp = 16.dp,
)

// 3. Compose 트리 전체에서 이 값들을 쉽게 꺼내 쓸 수 있도록 Local Provider 생성
// val LocalSpacing = staticCompositionLocalOf { Spacing() }
val LocalElevation = staticCompositionLocalOf { Elevation() }