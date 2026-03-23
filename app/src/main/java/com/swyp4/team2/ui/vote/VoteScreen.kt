package com.swyp4.team2.ui.vote

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.swyp4.team2.ui.component.ProfileImage
import com.swyp4.team2.ui.theme.*
import com.swyp4.team2.ui.vote.model.VoteUiModel
import com.swyp4.team2.ui.vote.model.VoteOptionUiModel
import com.swyp4.team2.ui.vote.model.VoteType

@Composable
fun VoteScreen(
    voteType: VoteType,
    uiModel: VoteUiModel,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit
) {
    val isPreVote = voteType == VoteType.PRE

    val bgColor = if (isPreVote) SwypTheme.colors.surface else Color.Black
    val titleColor = if (isPreVote) Gray900 else SwypTheme.colors.surface
    val descColor = if (isPreVote) Gray700 else Gray400

    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    val isButtonEnabled = selectedOptionId != null

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
                    text = if (isPreVote) stringResource(R.string.prevote) else "사후 투표하기", // 임시 텍스트
                    onClick = { selectedOptionId?.let { onVoteSubmit(it) } },
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                AsyncImage(
                    model = uiModel.bgImageUrl,
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomStart)
                        .padding(horizontal = 20.dp)
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        uiModel.tags.forEach { tag ->
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
                        text = uiModel.title.replace(", ", ",\n"),
                        style = SwypTheme.typography.h1SemiBold,
                        color = titleColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isPreVote) uiModel.preDescription else uiModel.postDescription,
                        style = SwypTheme.typography.b3Regular,
                        color = descColor
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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
                        option = uiModel.optionA, // 🌟 A 옵션 모델 통째로 넘기기
                        isSelected = selectedOptionId == uiModel.optionA.optionId,
                        onClick = { selectedOptionId = uiModel.optionA.optionId }
                    )
                    VoteOptionCard(
                        modifier = Modifier.weight(1f),
                        option = uiModel.optionB, // 🌟 B 옵션 모델 통째로 넘기기
                        isSelected = selectedOptionId == uiModel.optionB.optionId,
                        onClick = { selectedOptionId = uiModel.optionB.optionId }
                    )
                }

                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = CircleShape,
                    color = Color(0xFFF2E3C6)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("VS", style = SwypTheme.typography.labelMedium, color = Gray900)
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
    option: VoteOptionUiModel,
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
        ProfileImage(
            model = option.philosopherType.,
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = option.opinion, style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = option.philosopherName, style = SwypTheme.typography.labelXSmall, color = Gray500)
    }
}