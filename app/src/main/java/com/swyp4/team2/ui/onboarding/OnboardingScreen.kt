package com.swyp4.team2.ui.onboarding

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.PerspectiveStance
import com.swyp4.team2.domain.model.SpeakerType
import com.swyp4.team2.ui.component.ChatBubble
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.perspective.PerspectiveMenuItem
import com.swyp4.team2.ui.scenario.model.ScenarioScriptUiModel
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige300
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige700
import com.swyp4.team2.ui.theme.Beige900
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme
import java.time.format.TextStyle
import com.swyp4.team2.ui.theme.Pretendard
import com.swyp4.team2.ui.theme.Primary50
import com.swyp4.team2.ui.theme.Primary600

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

        Spacer(modifier = Modifier.height(24.dp))

        // 중앙 카드 영역 (높이 증가: aspectRation 대신 고정 높이 400dp, 불투명도 0.8 추가)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .alpha(0.8f) // 💡 모든 카드의 투명도를 0.8로 설정
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
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
    ) {
        // 채팅 리스트 영역
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FirstOnboardingChatBubble(
                profileResId = R.drawable.ic_profile_kant,
                name = "칸트",
                message = "인간은 짐승과 달리 스스로 세운 도덕 법칙에 복종할 수 있는 '이성적 존재'입니다.",
                isLeft = true
            )

            FirstOnboardingChatBubble(
                profileResId = R.drawable.ic_profile_niche,
                name = "니체",
                message = "이성이요? 당신은 그 차가운 이성으로 생동감 넘치는 삶의 본능을 죽이고 있습니다.",
                isLeft = false
            )

            FirstOnboardingChatBubble(
                profileResId = R.drawable.ic_profile_kant,
                name = "칸트",
                message = "삶의 목적은 '행복'이 아니라 '행복해질 자격'을 갖추는 것입니다. 그것은 도덕적 의무를 완수하는 삶이죠.",
                isLeft = true
            )
        }

        // 하단 그라데이션 페이드 효과
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
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
private fun FirstOnboardingChatBubble(
    profileResId: Int,
    name: String,
    message: String,
    isLeft: Boolean
) {
    val bubbleBgColor = if (isLeft) Color.White else Beige500
    val bubbleBorderColor = if (isLeft) Beige500 else Beige700

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        // [왼쪽 슬롯] : 왼쪽 화자(칸트)일 때만 프로필 노출
        Box(
            modifier = Modifier.width(36.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLeft) {
                Image(
                    painter = painterResource(id = profileResId),
                    contentDescription = name,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // [가운데 영역] : 이름 + 텍스트 말풍선
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = name,
                style = SwypTheme.typography.b3SemiBold,
                color = Gray400,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                textAlign = if (isLeft) TextAlign.Start else TextAlign.End
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bubbleBgColor, RoundedCornerShape(2.dp))
                    .border(1.dp, bubbleBorderColor, RoundedCornerShape(2.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = message,
                    style = SwypTheme.typography.b5Medium,
                    color = Gray700,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // [오른쪽 슬롯] : 오른쪽 화자(니체)일 때만 프로필 노출
        Box(
            modifier = Modifier.width(36.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (!isLeft) {
                Image(
                    painter = painterResource(id = profileResId),
                    contentDescription = name,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun SecondOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            // 1. 헤더 (생각이 바뀌었어요 & 비율 바)
            SecondOnboardingHeader()

            Spacer(modifier = Modifier.height(16.dp))

            // 2. 리스트 (하드코딩된 아이템 2개)
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondOnboardingItemCard(
                    profileResId = R.drawable.ic_profile_racoon,
                    nickname = "사유하는 라쿤",
                    replyCount = "23",
                    likeCount = "1,340"
                )
                SecondOnboardingItemCard(
                    profileResId = R.drawable.ic_profile_dochi,
                    nickname = "사유하는 고슴도치",
                    replyCount = "0",
                    likeCount = "0"
                )
            }
        }

        // 하단 그라데이션 페이드 효과
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
private fun SecondOnboardingHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // 생각이 바뀌었어요 버튼 (고정 UI)
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Surface(color = Primary50, shape = RoundedCornerShape(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(painter = painterResource(id = R.drawable.ic_think), contentDescription = null, tint = Primary500, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "생각이 바뀌었어요", style = SwypTheme.typography.caption2SemiBold, color = Primary500)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 비율 바 (고정 UI: 78% vs 22%)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "찬 78.0%", style = SwypTheme.typography.label, color = Gray600)
            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
            ) {
                Box(modifier = Modifier.weight(0.78f).fillMaxHeight().background(Color(0xFFA64D47)))
                Box(modifier = Modifier.weight(0.22f).fillMaxHeight().background(Color(0xFFEBEBEB)))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = "반 22.0%", style = SwypTheme.typography.label, color = Gray600)
        }
    }
}

@Composable
private fun SecondOnboardingItemCard(
    profileResId: Int,
    nickname: String,
    replyCount: String,
    likeCount: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(width = 1.dp, color = Beige600)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // 프로필 영역
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = profileResId),
                    contentDescription = null,
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = nickname, style = SwypTheme.typography.labelMedium, color = Gray700)
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = SwypTheme.colors.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text = "찬성", // 온보딩은 항상 '찬성' 고정
                                style = SwypTheme.typography.b5Medium,
                                color = SwypTheme.colors.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(text = "2분 전", style = SwypTheme.typography.labelXSmall, color = SwypTheme.colors.outline)
                }

                // 더보기 아이콘 (클릭 안 됨)
                Icon(painterResource(id = R.drawable.ic_more), null, tint = Gray300, modifier = Modifier.size(16.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 본문 영역 (고정 텍스트)
            Text(
                text = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.",
                style = SwypTheme.typography.b4Regular,
                color = Gray600,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 하단 영역 (더보기, 댓글 수, 좋아요 수)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "더보기", style = SwypTheme.typography.b5Medium, color = Gray300)
                Spacer(modifier = Modifier.weight(1f))

                Icon(painterResource(id = R.drawable.ic_message), null, Modifier.size(12.dp), tint = Gray300)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = replyCount, style = SwypTheme.typography.b5Medium, color = Gray300)

                Spacer(modifier = Modifier.width(12.dp))

                Icon(painterResource(id = R.drawable.ic_heart_plus), null, Modifier.size(12.dp), tint = Gray300)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = likeCount, style = SwypTheme.typography.b5Medium, color = Gray300)
            }
        }
    }
}


@Composable
fun ThirdOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, Beige700, RoundedCornerShape(2.dp))
                    .padding(16.dp)
            ) {
                // 상단: 태그 및 참여자 수
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(color = Beige400, shape = RoundedCornerShape(2.dp)) {
                        Text(
                            text = "#투표",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = SwypTheme.typography.b4Regular,
                            color = Primary500
                        )
                    }
                    Text(text = "985명 참여", style = SwypTheme.typography.label, color = Gray400)
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 중단: 퀴즈 제목
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "가상국가를 만든다면 대통령은 ",
                            style = SwypTheme.typography.b3SemiBold,
                            color = Gray900
                        )
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(20.dp)
                                .border(1.dp, Beige600, RoundedCornerShape(2.dp))
                                .background(Beige300)
                        )
                        Text(
                            text = "이다",
                            style = SwypTheme.typography.b3SemiBold,
                            color = Gray900
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "빈칸에 들어갈 가장 적절한 답을 골라주세요",
                        style = SwypTheme.typography.label,
                        color = Gray300,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 하단: 2x2 연한 베이지 버튼 그리드
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThirdOnboardingButton(modifier = Modifier.weight(1f), index = "1", text = "석가모니")
                            ThirdOnboardingButton(modifier = Modifier.weight(1f), index = "2", text = "세종대왕")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThirdOnboardingButton(modifier = Modifier.weight(1f), index = "3", text = "예수")
                            ThirdOnboardingButton(modifier = Modifier.weight(1f), index = "4", text = "일론 머스크")
                        }
                    }
                }
            }
        }

        // 하단 그라데이션 페이드 효과
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
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
private fun ThirdOnboardingButton(modifier: Modifier, index: String, text: String) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(Beige300)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "$index. ", style = SwypTheme.typography.labelXSmall, color = Color(0xFFCBA572)) // 옅은 금색 번호
        Text(text = text, style = SwypTheme.typography.label, color = Gray900)
    }
}

@Composable
fun FourthOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
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
                .height(140.dp)
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