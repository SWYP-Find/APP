package com.swyp4.team2.ui.vote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.home.model.BattleProfile
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige300
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige50
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige900
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary300
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.Secondary200
import com.swyp4.team2.ui.theme.Secondary500
import com.swyp4.team2.ui.theme.SwypTheme
import com.swyp4.team2.ui.vote.model.VoteTopicItem
import com.swyp4.team2.ui.vote.model.VoteType
@Composable
fun VoteScreen(
    voteType: VoteType,
    item: VoteTopicItem,
    onBackClick: () -> Unit,
    onVoteSubmit: () -> Unit
) {
    val isPreVote = voteType == VoteType.PRE

    // 테마 설정
    val bgColor = if (isPreVote) SwypTheme.colors.surface else Color.Black
    val titleColor = if (isPreVote) Gray900 else SwypTheme.colors.surface
    val descColor = if (isPreVote) Gray700 else Gray400

    // 상태 관리 (버튼 활성화)
    var selectedSide by remember { mutableStateOf<Int?>(null) }
    val isButtonEnabled = selectedSide != null

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = bgColor,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    centerTitle = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backIconColor = Color.White,
                    backgroundColor = Color.Transparent,
                    actions = {
                        IconButton(onClick = { /* 공유 로직 */ }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "공유",
                                tint = Color.White
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                CustomButton(
                    text = stringResource(R.string.prevote),
                    onClick = { if (isButtonEnabled) onVoteSubmit() },
                    modifier = Modifier.padding(20.dp),
                    backgroundColor = if (isButtonEnabled) SwypTheme.colors.primary else Primary300,
                    textColor = Beige50
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            // [상단] 배경 이미지 + 오버레이
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AsyncImage(
                    model = item.bgImageRes,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .drawWithCache {
                            onDrawWithContent {
                                drawContent()
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Black.copy(alpha = 0.3f),
                                            Color.Transparent,
                                            bgColor
                                        ),
                                        startY = 0f,
                                        endY = size.height
                                    )
                                )
                            }
                        },
                    contentScale = ContentScale.Crop
                )

                // 태그, 제목, 설명
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        item.tags.forEach { tag ->
                            Surface(
                                color = Color.White.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Text(
                                    text = "#$tag",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = SwypTheme.typography.label,
                                    color = Primary500
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = item.title.replace(", ", ",\n"),
                        style = SwypTheme.typography.h1SemiBold,
                        color = titleColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isPreVote) item.preDescription else item.postDescription,
                        style = SwypTheme.typography.b3Regular,
                        color = descColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // [하단] VS 카드 영역
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    VoteOptionCard(
                        modifier = Modifier.weight(1f),
                        profile = item.leftProfile,
                        isSelected = selectedSide == 0,
                        onClick = { selectedSide = 0 }
                    )
                    VoteOptionCard(
                        modifier = Modifier.weight(1f),
                        profile = item.rightProfile,
                        isSelected = selectedSide == 1,
                        onClick = { selectedSide = 1 }
                    )
                }

                // 정중앙 VS 원형 뱃지
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = Color(0xFFF2E3C6) // 베이지색 뱃지
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            "VS",
                            style = SwypTheme.typography.labelMedium,
                            color = Gray900
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun VoteOptionCard(
    modifier: Modifier = Modifier,
    profile: BattleProfile,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) Secondary500 else Beige500

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .background(Beige300)
            .clickable { onClick() }
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 프로필 아이콘 배경
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFEEEEEE), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = profile.profileImg), // R.drawable.ic_profile_xunzi 등
                contentDescription = null,
                modifier = Modifier.size(36.dp),
                tint = Color.Unspecified
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = profile.opinion, style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = profile.name, style = SwypTheme.typography.labelXSmall, color = Gray500)
    }
}