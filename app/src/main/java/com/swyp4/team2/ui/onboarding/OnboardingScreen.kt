package com.swyp4.team2.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.theme.Beige300
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun OnboardingScreen(
    onNavigateToMain: ()->Unit
) {
    val pageCount = 3
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = Modifier.fillMaxSize()
            .background(Beige300)
            .systemBarsPadding(),
    ){
        // 상단: 텍스트 및 중앙
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            OnboardingPageContent(page = page)
        }

        // 하단: 인디케이터 + 시작하기 버튼
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 페이지 인디케이터
            Row(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pageCount) { iteration ->
                    val color = if (pagerState.currentPage == iteration) Color(0xFF1F1F1F) else Color(0xFFD9D9D9)
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

            // 시작하기 버튼
            CustomButton(
                text = stringResource(R.string.onboarding_start),
                onClick = onNavigateToMain,
                backgroundColor = Primary500,
                textColor = Color.White
            )
        }
    }
}

// 각 페이지(1, 2, 3)의 내용을 그려주는 컴포저블
@Composable
fun OnboardingPageContent(page: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 120.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val title = when (page) {
            0 -> "AI 철학자들의 실시간 배틀"
            1 -> "매일 새로운 투표, 당신의 Pick은?"
            else -> "나와 가장 닮은 철학자는?"
        }

        val subTitle = when (page) {
            0 -> "위대한 사상가들의 토론을 듣고,\n당신의 입장을 선택하세요."
            1 -> "철학, 예술, 과학, 사회 등\n다양한 주제의 배틀과 투표가 기다리고 있어요."
            else -> "토론 성향에 따라 철학자 유형이 부여돼요.\n배틀에 참여해 새로운 나를 발견해보세요!"
        }

        // 타이틀
        Text(
            style = SwypTheme.typography.h2SemiBold,
            text = title,
            color = Gray900,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 서브 타이틀
        Text(
            text = subTitle,
            style = SwypTheme.typography.b2Medium,
            color = Gray400,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // 중앙
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            // TODO: 디자이너분께 피그마의 중앙 UI 부분을 3장의 '투명 배경 PNG 이미지'로 달라고 요청하세요!
            // 이미지를 받으면 R.drawable.ic_onboarding_1 처럼 넣으시면 됩니다.

            /* 실제 이미지 넣는 코드 예시:
            val imageRes = when (page) {
                0 -> R.drawable.img_onboarding_1
                1 -> R.drawable.img_onboarding_2
                else -> R.drawable.img_onboarding_3
            }
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "온보딩 이미지",
                modifier = Modifier.fillMaxSize()
            )
            */

            Text(
                text = "${page + 1}번째 이미지 들어갈 자리",
                color = Color.Gray
            )
        }
    }
}