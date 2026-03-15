package com.swyp4.team2.ui.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.swyp4.team2.R

// 1. Pretendard 폰트 패밀리 세팅 (ExtraBold 추가!)
val Pretendard = FontFamily(
    Font(R.font.pretendard_extrabold, FontWeight.ExtraBold),       // 800
    Font(R.font.pretendard_bold, FontWeight.Bold),           // 700
    Font(R.font.pretendard_semibold, FontWeight.SemiBold),   // 600
    Font(R.font.pretendard_medium, FontWeight.Medium),       // 500
    Font(R.font.pretendard_regular, FontWeight.Normal)       // 400
)

// 🌟 2. 피그마와 100% 동일한 이름의 데이터 클래스 생성
data class SwypTypography(
    // [ Headings ] - Letter Spacing -2.5% (-0.025 곱하기)
    val h0ExtraBold: TextStyle,
    val h1SemiBold: TextStyle,
    val h2SemiBold: TextStyle,
    val h3Bold: TextStyle,
    val h4SemiBold: TextStyle,
    val h5SemiBold: TextStyle,

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

// 🌟 3. 실제 수치 주입 (피그마 수치 완벽 반영)
val swypTypography = SwypTypography(
    // Headings (행간 128%, 자간 -2.5%)
    h0ExtraBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.ExtraBold, fontSize = 40.sp, lineHeight = 51.2.sp, letterSpacing = (-1.0).sp),
    h1SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 30.sp, lineHeight = 38.4.sp, letterSpacing = (-0.75).sp),
    h2SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 20.sp, lineHeight = 25.6.sp, letterSpacing = (-0.5).sp),
    h3Bold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 18.sp, lineHeight = 23.04.sp, letterSpacing = (-0.45).sp),
    h4SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 20.48.sp, letterSpacing = (-0.4).sp),
    h5SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 17.92.sp, letterSpacing = (-0.35).sp),

    // B1 (행간 150%)
    b1Regular = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    b1Medium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 16.sp, lineHeight = 24.sp),
    b1SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 24.sp),

    // B2 (행간 150%)
    b2SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, lineHeight = 22.5.sp),
    b2Medium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 15.sp, lineHeight = 22.5.sp),
    b2Bold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 22.5.sp),

    // B3 (행간 140%)
    b3Regular = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 19.6.sp),
    b3SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 14.sp, lineHeight = 19.6.sp),

    // B4 (행간 140%)
    b4Regular = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Normal, fontSize = 13.sp, lineHeight = 18.2.sp),
    b4Medium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 18.2.sp),

    // B5 (행간 140%)
    b5Medium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.8.sp),

    // Caption2 (행간 140%)
    caption2SemiBold = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 11.sp, lineHeight = 15.4.sp),
    caption2Medium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 15.4.sp),

    // Labels & Chip (행간 140%, 굵기는 이전 코드 기준 적용)
    labelLarge = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 22.4.sp),
    labelMedium = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 19.6.sp),
    label = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.8.sp),
    labelXSmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.SemiBold, fontSize = 10.sp, lineHeight = 14.sp),
    chipSmall = TextStyle(fontFamily = Pretendard, fontWeight = FontWeight.Medium, fontSize = 13.sp, lineHeight = 18.2.sp)
)

// 🌟 4. Compose 전역에서 쓸 수 있게 Local 객체 생성 (Theme.kt에서 사용)
val LocalSwypTypography = staticCompositionLocalOf { swypTypography }