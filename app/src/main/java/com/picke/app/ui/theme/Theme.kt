package com.picke.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Primary500,
    background = Beige50,
    surface = Color.White,
)

@Composable
fun SwypAppTheme(
    content: @Composable () -> Unit
) {
    // 🌟 1. 현재 화면(View) 정보를 가져옵니다.
    val view = LocalView.current

    // 🌟 2. 화면이 그려질 때마다 안드로이드 시스템의 고집을 꺾고 강제로 설정합니다.
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 핵심 1: 상태바와 하단바 배경을 '완벽한 투명'으로 만듭니다. (Edge-to-Edge 꽉 차게!)
            // 예전처럼 Beige50으로 칠해버리면 화면이 위로 밀려버리니 꼭 TRANSPARENT를 써야 합니다.
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            window.navigationBarColor = android.graphics.Color.TRANSPARENT

            // 핵심 2: "내 앱은 밝은 배경이니까 아이콘은 까맣게(Dark) 그려!" 하고 강제 명령을 내립니다.
            // true = 어두운 아이콘 적용
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = true
            insetsController.isAppearanceLightNavigationBars = true
        }
    }

    CompositionLocalProvider(
        LocalSwypTypography provides swypTypography,
        LocalElevation provides Elevation()
    ) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            content = content
        )
    }
}


object SwypTheme {
    val colors
        @Composable
        get() = MaterialTheme.colorScheme

    val typography: SwypTypography
        @Composable
        get() = LocalSwypTypography.current

    /*val spacing: Spacing
        @Composable
        get() = LocalSpacing.current*/

    val elevation: Elevation
        @Composable
        get() = LocalElevation.current
}