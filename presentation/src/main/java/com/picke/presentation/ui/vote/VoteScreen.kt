package com.picke.presentation.ui.vote

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import com.picke.domain.feature.battle.model.BattleDetailBoard
import com.picke.domain.feature.battle.model.BattleOptionBoard
import com.picke.presentation.R
import com.picke.presentation.analytics.ShareChannel
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomSingleActionDialog
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.component.ShareDialog
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.SwypTheme
import com.picke.presentation.util.shareBattleToInstagramStoryBrightMode
import com.picke.presentation.util.shareBattleToInstagramStoryDarkMode
import com.picke.presentation.util.shareBattleToKakao
import kotlinx.coroutines.launch

@Composable
fun VoteRoute(
    voteType: VoteType,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    BackHandler {
        onBackClick()
    }
    val uiState by viewModel.uiState.collectAsState()

    if (uiState.isLoading) {
        VoteSkeleton(voteType = voteType, modifier = Modifier.fillMaxSize())
    } else {
        val detail = uiState.battleDetail
        if (detail != null) {
            VoteScreen(
                voteType = voteType,
                battleDetail = detail,
                uiState = uiState,
                onBackClick = onBackClick,
                onVoteSubmit = onVoteSubmit,
                onNavigateToExplore = onNavigateToExplore,
                viewModel = viewModel
            )
        } else {
            BattleNotFoundScreen(onBackClick = onBackClick)
        }
    }
}

// 존재하지 않거나 삭제된 배틀 id로 진입했을 때 보여주는 빈 화면
// (관점 화면의 빈 목록 상태와 동일한 Picke 로고 + 안내 문구 UI를 재사용)
@Composable
private fun BattleNotFoundScreen(onBackClick: () -> Unit) {
    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.backgroundBrand,
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_picke),
                contentDescription = "빈 화면 로고",
                modifier = Modifier.size(width = 160.dp, height = 120.dp),
                tint = SwypTheme.colors.borderDefault
            )
            // Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "해당 배틀은 존재하지 않습니다",
                style = SwypTheme.typography.b3Regular,
                color = SwypTheme.colors.beige800
            )
        }
    }
}

@Composable
fun VoteScreen(
    voteType: VoteType,
    battleDetail: BattleDetailBoard,
    uiState: VoteUiState,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    viewModel: VoteViewModel
) {
    val isPreVote = voteType == VoteType.PRE
    val battleInfo = battleDetail.battleInfo

    val bgColor = if (isPreVote) SwypTheme.colors.surface else Color.Black
    val titleColor = if (isPreVote) SwypTheme.colors.textPrimary else SwypTheme.colors.surface
    val descColor = if (isPreVote) SwypTheme.colors.textSecondary else SwypTheme.colors.neutral400

    var selectedOptionId by remember { mutableStateOf<String?>(null) }
    val isButtonEnabled = selectedOptionId != null

    val context = androidx.compose.ui.platform.LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val clipboardManager = androidx.compose.ui.platform.LocalClipboardManager.current
    val graphicsLayer = androidx.compose.ui.graphics.rememberGraphicsLayer()
    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    // 공유하기 함수
    val onKakaoShareClick = {
        isSharing = true
        coroutineScope.launch {
            try {
                val request = coil.request.ImageRequest.Builder(context)
                    .data(battleInfo.thumbnailUrl)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                val bitmap = (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap

                if (bitmap != null) {
                    shareBattleToKakao(
                        context = context,
                        bitmap = bitmap,
                        battleId = battleInfo.battleId,
                        battleTitle = battleInfo.title,
                        battleDescription = if (isPreVote) battleInfo.summary else battleDetail.description,
                        onComplete = { isSharing = false }
                    )
                } else {
                    isSharing = false
                    android.widget.Toast.makeText(
                        context,
                        "이미지 로드 실패",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                isSharing = false
                android.widget.Toast.makeText(context, "공유 실패", android.widget.Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launch {
            try {
                kotlinx.coroutines.delay(100)

                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()

                if (isPreVote) {
                    shareBattleToInstagramStoryBrightMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false }
                    )
                } else {
                    shareBattleToInstagramStoryDarkMode(
                        context = context,
                        bitmap = bitmap,
                        onComplete = { isSharing = false }
                    )
                }

            } catch (e: Exception) {
                isSharing = false
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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
                            Icon(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { showShareDialog = true },
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "공유",
                                tint = Color.White
                            )
                        }
                    )
                }
            },
            bottomBar = {
                Box(modifier = Modifier.navigationBarsPadding()) {
                    CustomButton(
                        text = if (isPreVote) stringResource(R.string.prevote) else "최종 투표하기",
                        onClick = {
                            if (selectedOptionId != null) {
                                viewModel.submitVote(
                                    voteType = voteType,
                                    selectedOptionId = selectedOptionId!!,
                                    onSuccess = {
                                        /*val props = JSONObject().apply {
                                        put("battle_id", battleInfo.battleId.toString())
                                        put("battle_title", battleInfo.title)
                                    }

                                    if (isPreVote) {
                                        SwypApplication.mixpanel.track("pre_vote", props) // 기획서 명칭 일치
                                    } else {
                                        SwypApplication.mixpanel.track("post_vote", props) // 기획서 명칭 일치
                                    }*/

                                        onVoteSubmit(battleInfo.battleId.toString())
                                    }
                                )
                            }
                        },
                        modifier = Modifier.padding(20.dp),
                        backgroundColor = if (isButtonEnabled) SwypTheme.colors.primary else SwypTheme.colors.primaryDisabled,
                        textColor = SwypTheme.colors.surfaceDefault
                    )
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(bgColor)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
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
                            Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer(
                                        baseColor = if (isPreVote) null else SwypTheme.colors.neutral600,
                                        highlightColor = if (isPreVote) null else SwypTheme.colors.neutral400
                                    )
                            )
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
                                        modifier = Modifier.padding(
                                            horizontal = 8.dp,
                                            vertical = 2.dp
                                        ),
                                        style = SwypTheme.typography.label,
                                        color = SwypTheme.colors.primary
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
                            text = battleDetail.description,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (battleInfo.options.size >= 2) {
                            VoteOptionCard(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .fillMaxHeight(),
                                option = battleInfo.options[0],
                                isSelected = selectedOptionId == battleInfo.options[0].optionId,
                                onClick = { selectedOptionId = battleInfo.options[0].optionId }
                            )
                            VoteOptionCard(
                                modifier = Modifier
                                    .weight(0.5f)
                                    .fillMaxHeight(),
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
                            Text(
                                "VS",
                                style = SwypTheme.typography.labelMedium,
                                color = SwypTheme.colors.textPrimary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        // 공유하기 다이얼로그
        if (showShareDialog) {
            ShareDialog(
                onDismiss = { showShareDialog = false },
                onKakaoClick = {
                    showShareDialog = false
                    viewModel.trackShare(ShareChannel.KAKAO)
                    onKakaoShareClick()
                },
                onInstaClick = {
                    showShareDialog = false
                    viewModel.trackShare(ShareChannel.INSTAGRAM)
                    onInstaShareClick()
                },
                onFacebookClick = {
                    showShareDialog = false
                },
                onCopyLinkClick = {
                    showShareDialog = false
                    viewModel.trackShare(ShareChannel.LINK)
                    viewModel.getShareLink(
                        battleId = battleInfo.battleId.toInt(),
                        onSuccess = { url ->
                            clipboardManager.setText(
                                androidx.compose.ui.text.AnnotatedString(
                                    url
                                )
                            )
                            android.widget.Toast.makeText(
                                context,
                                "링크가 클립보드에 복사되었습니다.",
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        },
                        onError = { errorMessage ->
                            android.widget.Toast.makeText(
                                context,
                                errorMessage,
                                android.widget.Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            )
        }

        if (isSharing) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .pointerInput(Unit) {},
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SwypTheme.colors.primaryDarkest)
            }
        }

        if (uiState.isInsufficientPoints) {
            CustomSingleActionDialog(
                message = "컨텐츠를 시청하기 위한\n포인트가 부족해요!",
                subMessage = "매일 출석체크만 해도 5P를 받을 수 있어요!",
                buttonText = "배틀 주제 구경하러 가기",
                onDismiss = { viewModel.dismissPointDialog() },
                onConfirm = {
                    viewModel.dismissPointDialog()
                    onNavigateToExplore()
                }
            )
        }
    }
}

@Composable
fun VoteOptionCard(
    modifier: Modifier = Modifier,
    option: BattleOptionBoard,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor =
        if (isSelected) SwypTheme.colors.secondary else SwypTheme.colors.borderDisabled
    val contentAlpha = if (isSelected) 1f else 0.8f

    Column(
        modifier = modifier
            .alpha(contentAlpha)
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, borderColor, RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surfaceSubtle)
            .clickable { onClick() }
            .padding(vertical = 24.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ProfileImage(
            model = option.imageUrl,
            modifier = Modifier.size(40.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = option.title,
            style = SwypTheme.typography.h4SemiBold,
            color = SwypTheme.colors.textPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = option.representative,
            style = SwypTheme.typography.labelXSmall,
            color = SwypTheme.colors.textTertiary
        )
    }
}
