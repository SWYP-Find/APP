package com.swyp4.team2.ui.onboarding

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.PerspectiveStance
import com.swyp4.team2.domain.model.SpeakerType
import com.swyp4.team2.ui.component.ChatBubble
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.home.TodayPickeCard
import com.swyp4.team2.ui.home.model.TodayPickUiModel
import com.swyp4.team2.ui.perspective.PerspectiveHeader
import com.swyp4.team2.ui.perspective.PerspectiveItemCard
import com.swyp4.team2.ui.perspective.model.PerspectiveUiModel
import com.swyp4.team2.ui.scenario.model.ScenarioScriptUiModel
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige300
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme
import java.time.format.TextStyle
import com.swyp4.team2.ui.theme.Pretendard

@Composable
fun OnboardingScreen(
    onNavigateToLogin: ()->Unit
) {
    val pageCount = 4
    val pagerState = rememberPagerState(pageCount = { pageCount })

    Column(
        modifier = Modifier
            .fillMaxSize()
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
                onClick = onNavigateToLogin,
                backgroundColor = Primary500,
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
            .padding(top = 120.dp),
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

        // 타이틀
        Text(
            style = androidx.compose.ui.text.TextStyle(
                fontFamily = Pretendard,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp,
                lineHeight = 30.72.sp,
                letterSpacing = (-0.6).sp
            ),
            text = title,
            color = Gray900,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))

        // 서브 타이틀
        Text(
            text = subTitle,
            style = SwypTheme.typography.b2Medium,
            color = Gray300,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 중앙
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
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

@Composable
fun FirstOnboardingCard(modifier: Modifier = Modifier) {
    val onboardingScripts = listOf(
        ScenarioScriptUiModel(
            scriptId = "1",
            startTimeMs = 0,
            speakerType = SpeakerType.A,
            speakerName = "칸트",
            displayText = "인간은 짐승과 달리 스스로 세운 도덕 법칙에 복종할 수 있는 '이성적 존재'입니다.",
            profileImageUrl = R.drawable.ic_profile_kant
        ),
        ScenarioScriptUiModel(
            scriptId = "2",
            startTimeMs = 0,
            speakerType = SpeakerType.B,
            speakerName = "니체",
            displayText = "이성이요? 당신은 그 차가운 이성으로 생동감 넘치는 삶의 본능을 죽이고 있습니다.",
            profileImageUrl = R.drawable.ic_profile_niche
        ),
        ScenarioScriptUiModel(
            scriptId = "3",
            startTimeMs = 0,
            speakerType = SpeakerType.A,
            speakerName = "칸트",
            displayText = "삶의 목적은 '행복'이 아니라 '행복해질 자격'을 갖추는 것입니다. 그것은 도덕적 의무를 완수하는 삶이죠.",
            profileImageUrl = R.drawable.ic_profile_kant
        )
    )

    Box(
        modifier = modifier.fillMaxSize()
            .background(Color.White, RoundedCornerShape(2.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            onboardingScripts.forEach { script ->
                ChatBubble(
                    script = script,
                    isActive = false,
                    showAvatarAndName = true
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Beige300
                        )
                    )
                )
        )
    }
}

@Composable
fun SecondOnboardingCard(modifier: Modifier = Modifier) {
    val dummyPerspectives = listOf(
        PerspectiveUiModel(
            commentId = "1",
            profileImageRes = R.drawable.ic_profile_racoon,
            nickname = "사유하는 라쿤",
            stance = PerspectiveStance.AGREE,
            content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.",
            timeAgo = "2분 전",
            replyCount = 23,
            likeCount = 1340,
            isLiked = false
        ),
        PerspectiveUiModel(
            commentId = "2",
            profileImageRes = R.drawable.ic_profile_dochi,
            nickname = "사유하는 고슴도치",
            stance = PerspectiveStance.AGREE, // 찬성
            content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.",
            timeAgo = "2분 전",
            replyCount = 0,
            likeCount = 0,
            isLiked = false
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(2.dp))
    ) {
        // 2. 카드 내용물 (헤더 + 리스트)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            PerspectiveHeader(
                proPercentage = 78f,
                conPercentage = 22f
            )

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                dummyPerspectives.forEach { item ->
                    PerspectiveItemCard(
                        item = item,
                        isDetail = false,
                        onMoreClick = {},
                        clickable = false
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Beige300
                        )
                    )
                )
        )
    }
}

@Composable
fun ThirdOnboardingCard(modifier: Modifier = Modifier) {
    val dummyQuizItem = TodayPickUiModel.QuizPick(
        contentId = "dummy_1",
        title = "가상국가를 만든다면 대통령은 [    ] 이다",
        summary = "빈칸에 들어갈 가장 적절한 답을 골라주세요",
        options = listOf("석가모니", "세종대왕", "예수", "일론 머스크"),
        participantsText = "985명 참여"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White, RoundedCornerShape(2.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TodayPickeCard(
                item = dummyQuizItem,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Beige300
                        )
                    )
                )
        )
    }
}

@Composable
fun FourthOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .border(1.dp, Beige400, RoundedCornerShape(2.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(Primary500)
                )

                // 내부 컨텐츠 영역
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "나의 철학자 유형",
                        style = SwypTheme.typography.labelMedium,
                        color = Primary500
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "칸트형",
                        style = SwypTheme.typography.h3Bold,
                        color = Gray600
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Image(
                        painter = painterResource(id = R.drawable.ic_profile_kant),
                        contentDescription = "칸트 프로필",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Beige100)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "결과보다 과정을 중시하고, 보편적 도덕 법칙을 따르는 원칙주의자. 어떤 상황에서도 흔들리지 않는 기준을 가진 사람입니다.",
                        style = SwypTheme.typography.b5Medium,
                        color = Gray600,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // 키워드 태그 리스트
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val tags = listOf("의무론", "규칙 중시", "보편 원칙", "이상적")
                        tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .border(1.dp, Beige600, RoundedCornerShape(2.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    style = SwypTheme.typography.labelXSmall,
                                    color = Color(0xFF8C3E26)
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            Beige300
                        )
                    )
                )
        )
    }
}