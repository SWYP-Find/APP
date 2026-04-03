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

    // 🌟 2. 화면이 렌더링될 때 상태바 색상을 세팅하는 마법의 주문!
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window

            // 상태바 배경색을 우리 앱의 배경색(Beige50)으로 맞추거나 투명하게 만듭니다.
            window.statusBarColor = Beige50.toArgb()

            // 🚀 핵심: "배경이 밝으니까(Light), 아이콘은 어둡게 해줘!" (true = 어두운 아이콘)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
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