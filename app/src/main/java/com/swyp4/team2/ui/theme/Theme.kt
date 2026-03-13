package com.swyp4.team2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    // 1. 핵심 포인트 컬러
    primary = Primary500,
    onPrimary = Color.White, // Primary 위에는 흰색 글씨 쓰겠다!

    // 2. 서브 포인트 컬러
    secondary = Secondary500,
    onSecondary = Color.White,

    // 3. 배경 및 표면(카드) 컬러
    background = Beige50,
    onBackground = Gray900, // 배경 위에는 진한 회색 글씨 쓰겠다!
    surface = Color.White,
    onSurface = Gray900,

    // 4. (추가하면 좋은 것들) 보조 표면, 테두리, 에러
    surfaceVariant = Beige100, // 살짝 색깔 있는 카드 배경용
    outline = Gray300,         // 텍스트 입력창이나 버튼의 연한 테두리용
    error = Color(0xFFB3261E)  // 에러 났을 때 쓸 빨간색 (Material 기본 에러색)
)

@Composable
fun SwypAppTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalSpacing provides Spacing(),
        LocalElevation provides Elevation()
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content
        )
    }
}

object SwypTheme {
    val colors
        @Composable
        get() = MaterialTheme.colorScheme

    val typography
        @Composable
        get() = MaterialTheme.typography

    val spacing: Spacing
        @Composable
        get() = LocalSpacing.current

    val elevation: Elevation
        @Composable
        get() = LocalElevation.current
}