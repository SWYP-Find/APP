package com.picke.presentation.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.onboarding.component.FirstOnboardingCard
import com.picke.presentation.ui.onboarding.component.FourthOnboardingCard
import com.picke.presentation.ui.onboarding.component.SecondOnboardingCard
import com.picke.presentation.ui.onboarding.component.ThirdOnboardingCard
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.theme.Pretendard

@Composable
fun OnboardingScreen(onNavigateToLogin: () -> Unit) {
    val pageCount = 4
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(PickeTheme.colors.backgroundSubtle)
            .systemBarsPadding(),
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = page)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) {
                        Color(0xFF1F1F1F)
                    } else {
                        Color(0xFFD9D9D9)
                    }
                    val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .clip(CircleShape)
                            .background(color)
                            .height(8.dp)
                            .width(width)
                    )
                }
            }

            CustomButton(
                text = stringResource(R.string.onboarding_start),
                onClick = onNavigateToLogin,
                backgroundColor = PickeTheme.colors.primary,
                textColor = Color.White
            )
        }
    }
}

@Composable
fun OnboardingPageContent(page: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 92.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val title = when (page) {
            0 -> "AI 철학자들의 실시간 배틀"
            1 -> "배틀 승리로 주어지는 포인트"
            2 -> "매일 새로운 투표, 당신의 Pick은?"
            else -> "나와 가장 닮은 철학자는?"
        }
        val subTitle = when (page) {
            0 -> "위대한 사상가들의 토론을 듣고,\n당신의 입장을 선택하세요."
            1 -> "배틀 참여로 포인트를 모아\n나만의 배틀을 제안해보세요."
            2 -> "철학, 예술, 과학, 사회 등\n다양한 주제의 배틀과 투표가 기다리고 있어요."
            else -> "토론 성향에 따라 철학자 유형이 부여돼요.\n배틀에 참여해 새로운 나를 발견해보세요!"
        }

        Text(
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 30.72.sp,
                letterSpacing = (-0.6).sp
            ),
            text = title,
            color = PickeTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = subTitle,
            style = PickeTheme.typography.b2Medium,
            color = PickeTheme.colors.textMuted,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .alpha(0.8f)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            when (page) {
                0 -> FirstOnboardingCard()
                1 -> SecondOnboardingCard()
                2 -> ThirdOnboardingCard()
                3 -> FourthOnboardingCard()
            }
        }
    }
}

@Preview(showBackground = true, name = "Onboarding - 1페이지: AI 배틀")
@Composable
private fun OnboardingScreenPreview1() {
    PickeTheme { OnboardingPageContent(page = 0) }
}

@Preview(showBackground = true, name = "Onboarding - 2페이지: 포인트")
@Composable
private fun OnboardingScreenPreview2() {
    PickeTheme { OnboardingPageContent(page = 1) }
}

@Preview(showBackground = true, name = "Onboarding - 3페이지: 투표")
@Composable
private fun OnboardingScreenPreview3() {
    PickeTheme { OnboardingPageContent(page = 2) }
}

@Preview(showBackground = true, name = "Onboarding - 4페이지: 철학자 유형")
@Composable
private fun OnboardingScreenPreview4() {
    PickeTheme { OnboardingPageContent(page = 3) }
}