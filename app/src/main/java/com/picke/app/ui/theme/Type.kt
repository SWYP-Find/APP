package com.picke.app.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.picke.app.R

// 1. Pretendard 폰트 패밀리 세팅
val Pretendard = FontFamily(
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),   // 800
    Font(R.font.pretendard_bold, FontWeight.Bold),                      // 700
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),              // 600
    Font(R.font.pretendard_medium, FontWeight.Medium),                  // 500
    Font(R.font.pretendard_regular, FontWeight.Normal)                  // 400
)

// 2. 피그마와 100% 동일한 이름의 데이터 클래스 생성
data class SwypTypography(
    // [ Headings ] - Letter Spacing -2.5% (-0.025 곱하기)
    val h0SemiBold: TextStyle,
    val h1SemiBold: TextStyle,
    val h2SemiBold: TextStyle,
    val h3SemiBold: TextStyle,
    val h4SemiBold: TextStyle,

    // [ Body ]
    val b1Regular: TextStyle,
    val b1Medium: TextStyle,
    val b1SemiBold: TextStyle,

    val b2SemiBold: TextStyle,
    val b2Medium: TextStyle,
    val b2Bold: TextStyle,

    val b3Regular: TextStyle,
    val b3SemiBold: TextStyle,

    val b4Regular: TextStyle,
    val b4Medium: TextStyle,

    val b5Medium: TextStyle,

    // [ Caption & Labels ]
    val caption2SemiBold: TextStyle,
    val caption2Medium: TextStyle,

    val labelLarge: TextStyle,
    val labelMedium: TextStyle,
    val label: TextStyle,
    val labelXSmall: TextStyle,

    val chipSmall: TextStyle
)

// 3. 실제 수치 주입
val swypTypography = SwypTypography(
    // Headings
    h0SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 1.28.em,
        letterSpacing = (-0.025).em
    ),
    h1SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 1.28.em,
        letterSpacing = (-0.025).em
    ),
    h2SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 1.28.em,
        letterSpacing = (-0.025).em
    ),
    h3SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 1.28.em,
        letterSpacing = (-0.025).em
    ),
    h4SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 1.28.em,
        letterSpacing = (-0.025).em
    ),

    // B1
    b1Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 1.50.em
    ),
    b1Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    b1SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),

    // B2 
    b2SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.5.sp
    ),
    b2Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 22.5.sp
    ),
    b2Bold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 22.5.sp
    ),

    // B3 
    b3Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 19.6.sp
    ),
    b3SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 19.6.sp
    ),

    // B4 
    b4Regular = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 13.sp,
        lineHeight = 18.2.sp
    ),
    b4Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.2.sp
    ),

    // B5 
    b5Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.8.sp
    ),

    // Caption2 
    caption2SemiBold = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 15.4.sp
    ),
    caption2Medium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 15.4.sp
    ),

    // Labels & Chip 
    labelLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.4.sp
    ),
    labelMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 19.6.sp
    ),
    label = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.8.sp
    ),
    labelXSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 10.sp,
        lineHeight = 14.sp
    ),
    chipSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 18.2.sp
    ),
)

// 4. Compose 전역에서 쓸 수 있게 Local 객체 생성 (Theme.kt에서 사용)
val LocalSwypTypography = staticCompositionLocalOf { swypTypography }