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
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.BattleDetailBoard
import com.swyp4.team2.domain.model.BattleOptionBoard
import com.swyp4.team2.domain.model.VoteBoard
import com.swyp4.team2.domain.model.VoteOption
import com.swyp4.team2.ui.component.CustomButton
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.ProfileImage
import com.swyp4.team2.ui.theme.*

@Composable
fun VoteRoute(
    voteType: VoteType,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Primary900)
        }
    } else {
        uiState.battleDetail?.let { detail ->
            VoteScreen(
                voteType = voteType,
                battleDetail = detail,
                onBackClick = onBackClick,
                onVoteSubmit = onVoteSubmit,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun VoteScreen(
    voteType: VoteType,
    battleDetail: BattleDetailBoard,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    viewModel: VoteViewModel
) {
    val isPreVote = voteType == VoteType.PRE
    val battleInfo = battleDetail.battleInfo

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
                    /*actions = {
                        IconButton(onClick = { *//* 공유 로직 *//* }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "공유",
                                tint = Color.White
                            )
                        }
                    }*/
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                CustomButton(
                    text = if (isPreVote) stringResource(R.string.prevote) else "사후 투표하기",
                    onClick = {
                        if (selectedOptionId != null) {
                            viewModel.submitVote(
                                voteType = voteType,
                                selectedOptionId = selectedOptionId!!,
                                onSuccess = {
                                    onVoteSubmit(battleInfo.battleId.toString())
                                }
                            )
                        }
                    },
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
                SubcomposeAsyncImage(
                    model = battleInfo.thumbnailUrl,
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
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = SwypTheme.colors.primary,
                                modifier = Modifier.size(44.dp)
                            )
                        }
                    }
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
                        battleInfo.tags.forEach { tag ->
                            Surface(
                                color = Color.White.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Text(
                                    text = "#${tag.name}",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = SwypTheme.typography.label,
                                    color = Primary500
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = battleInfo.title.replace(", ", ",\n"),
                        style = SwypTheme.typography.h1SemiBold,
                        color = titleColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isPreVote) battleInfo.summary else battleDetail.description,
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
                    if (battleInfo.options.size >= 2) {
                        VoteOptionCard(
                            modifier = Modifier.weight(0.5f),
                            option = battleInfo.options[0],
                            isSelected = selectedOptionId == battleInfo.options[0].optionId,
                            onClick = { selectedOptionId = battleInfo.options[0].optionId}
                        )
                        VoteOptionCard(
                            modifier = Modifier.weight(0.5f),
                            option = battleInfo.options[1],
                            isSelected = selectedOptionId == battleInfo.options[1].optionId,
                            onClick = { selectedOptionId = battleInfo.options[1].optionId }
                        )
                    }
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

// 3. 개별 옵션 카드
@Composable
fun VoteOptionCard(
    modifier: Modifier = Modifier,
    option: BattleOptionBoard,
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
            model = option.imageUrl,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = option.title, style = SwypTheme.typography.h4SemiBold, color = Gray900)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = option.representative, style = SwypTheme.typography.labelXSmall, color = Gray500)
    }
}