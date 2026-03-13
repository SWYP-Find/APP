package com.swyp4.team2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.swyp4.team2.R

// 1. Pretendard 폰트 패밀리 세팅 (Weight 700, 600, 500, 400)
val Pretendard = FontFamily(
    Font(R.font.pretendard_bold, FontWeight.Bold),  // 700
    Font(R.font.pretendard_semibold, FontWeight.SemiBold), // 600
    Font(R.font.pretendard_medium, FontWeight.Medium),     // 500
    Font(R.font.pretendard_regular, FontWeight.Normal)     // 400
)

// 2. Material 3 Typography 매핑
val Typography = Typography(
    // H0. heading-xxlarge (30, 600, 128%, -2.5%)
    displayLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 38.4.sp,
        letterSpacing = (-0.75).sp
    ),

    // H1. heading-xlarge (30, 600, 128%, -2.5%)
    displayMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 30.sp,
        lineHeight = 38.4.sp,
        letterSpacing = (-0.75).sp
    ),

    // H2. heading-large (20, 600, 128%, -2.5%)
    headlineLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 25.6.sp,
        letterSpacing = (-0.5).sp
    ),

    // H3. heading-medium (16, 600, 128%, -2.5%)
    headlineMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 20.48.sp,
        letterSpacing = (-0.4).sp
    ),

    // H4. heading-small (14, 600, 128%, -2.5%)
    headlineSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        lineHeight = 17.92.sp,
        letterSpacing = (-0.35).sp
    ),

    // B1. body-large (16, 400, 150%, 0%)
    bodyLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.sp
    ),

    // B2. body-medium (14, 400, 150%, 0%)
    bodyMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
        letterSpacing = 0.sp
    ),

    // B3. body-small (12, 400, 180%, 0%)
    bodySmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 21.6.sp,
        letterSpacing = 0.sp
    ),

    // Label-large (16, 700, 140%, 0%)
    labelLarge = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Bold, // 700
        fontSize = 16.sp,
        lineHeight = 22.4.sp,
        letterSpacing = 0.sp
    ),

    // Label-medium (14, 500, 140%, 0%)
    labelMedium = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 14.sp,
        lineHeight = 19.6.sp,
        letterSpacing = 0.sp
    ),

    // Label-small (12, 500, 140%, 0%)
    labelSmall = TextStyle(
        fontFamily = Pretendard,
        fontWeight = FontWeight.Medium, // 500
        fontSize = 12.sp,
        lineHeight = 16.8.sp,
        letterSpacing = 0.sp
    )
)

// 3. Material 3 슬롯에 없는 커스텀 스타일
// Label-xsmall (10, 600, 140%, 0%)
val LabelXSmall = TextStyle(
    fontFamily = Pretendard,
    fontWeight = FontWeight.SemiBold, // 600
    fontSize = 10.sp,
    lineHeight = 14.sp,
    letterSpacing = 0.sp
)