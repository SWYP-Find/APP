package com.picke.presentation.ui.vote

import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.picke.presentation.R
import com.picke.presentation.analytics.ShareChannel
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.CustomSingleActionDialog
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.ShareDialog
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.vote.component.VoteOptionCard
import com.picke.presentation.ui.vote.component.VoteSkeleton
import com.picke.presentation.ui.vote.model.BattleDetailUiModel
import com.picke.presentation.ui.vote.model.VoteType
import com.picke.presentation.ui.vote.model.VoteUiState
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.shareBattleToInstagramStoryBrightMode
import com.picke.presentation.util.shareBattleToInstagramStoryDarkMode
import com.picke.presentation.util.shareBattleToKakao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun VoteRoute(
    voteType: VoteType,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    viewModel: VoteViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    BackHandler {
        onBackClick()
    }

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
                onSubmitVote = { selectedOptionId, onSuccess ->
                    viewModel.submitVote(
                        voteType = voteType,
                        selectedOptionId = selectedOptionId,
                        onSuccess = onSuccess
                    )
                },
                onTrackShare = { channel ->
                    viewModel.trackShare(channel)
                },
                onGetShareLink = { battleId, onSuccess, onError ->
                    viewModel.getShareLink(
                        battleId = battleId,
                        onSuccess = onSuccess,
                        onError = onError
                    )
                },
                onDismissPointDialog = {
                    viewModel.dismissPointDialog()
                }
            )
        } else {
            BattleNotFoundScreen(onBackClick = onBackClick)
        }
    }
}

@Composable
fun VoteScreen(
    voteType: VoteType,
    battleDetail: BattleDetailUiModel,
    uiState: VoteUiState,
    onBackClick: () -> Unit,
    onVoteSubmit: (String) -> Unit,
    onNavigateToExplore: () -> Unit,
    onSubmitVote: (String, () -> Unit) -> Unit,
    onTrackShare: (String) -> Unit,
    onGetShareLink: (Int, (String) -> Unit, (String) -> Unit) -> Unit,
    onDismissPointDialog: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }
    var selectedOptionId by remember { mutableStateOf<String?>(null) }

    val isPreVote = voteType == VoteType.PRE
    val battleInfo = battleDetail.battleInfo

    val backgroundColor =
        if (selectedOptionId != null) PickeTheme.colors.primary else PickeTheme.colors.primaryDisabled
    val bgColor = if (isPreVote) PickeTheme.colors.surface else Color.Black
    val titleColor = if (isPreVote) PickeTheme.colors.textPrimary else PickeTheme.colors.surface
    val descColor = if (isPreVote) PickeTheme.colors.textSecondary else PickeTheme.colors.neutral400

    val onKakaoShareClick = {
        isSharing = true
        coroutineScope.launch {
            try {
                val request = ImageRequest.Builder(context)
                    .data(battleInfo.thumbnailUrl)
                    .allowHardware(false)
                    .build()
                val result = context.imageLoader.execute(request)
                val bitmap = (result.drawable as? BitmapDrawable)?.bitmap

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
                    Toast.makeText(
                        context,
                        "이미지 로드 실패",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (_: Exception) {
                isSharing = false
                Toast.makeText(context, "공유 실패", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launch {
            try {
                delay(100.milliseconds)

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

            } catch (_: Exception) {
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
                                onSubmitVote(selectedOptionId!!) {
                                    /*val props = JSONObject().apply {
                                        put("battle_id", battleInfo.battleId.toString())
                                        put("battle_title", battleInfo.title)
                                    }

                                    if (isPreVote) {
                                        SwypApplication.mixpanel.track("pre_vote", props) // 기획서 명칭 일치
                                    } else {
                                        SwypApplication.mixpanel.track("post_vote", props) // 기획서 명칭 일치
                                    }*/
                                    onVoteSubmit(battleInfo.battleId)
                                }
                            }
                        },
                        modifier = Modifier.padding(20.dp),
                        backgroundColor = backgroundColor,
                        textColor = PickeTheme.colors.surfaceDefault
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
                                        baseColor = if (isPreVote) null else PickeTheme.colors.neutral600,
                                        highlightColor = if (isPreVote) null else PickeTheme.colors.neutral400
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
                                        style = PickeTheme.typography.label,
                                        color = PickeTheme.colors.primary
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = battleInfo.title.replace(", ", ",\n"),
                            style = PickeTheme.typography.h1SemiBold,
                            color = titleColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = battleDetail.description,
                            style = PickeTheme.typography.b3Regular,
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
                                text = "VS",
                                style = PickeTheme.typography.labelMedium,
                                color = PickeTheme.colors.textPrimary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        if (showShareDialog) {
            ShareDialog(
                onDismiss = { showShareDialog = false },
                onKakaoClick = {
                    showShareDialog = false
                    onTrackShare(ShareChannel.KAKAO)
                    onKakaoShareClick()
                },
                onInstaClick = {
                    showShareDialog = false
                    onTrackShare(ShareChannel.INSTAGRAM)
                    onInstaShareClick()
                },
                onFacebookClick = {
                    showShareDialog = false
                },
                onCopyLinkClick = {
                    showShareDialog = false
                    onTrackShare(ShareChannel.LINK)
                    onGetShareLink(
                        battleInfo.battleId.toInt(),
                        { url ->
                            clipboardManager.setText(AnnotatedString(url))
                            Toast.makeText(context, "링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                        },
                        { errorMessage ->
                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
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
                CircularProgressIndicator(color = PickeTheme.colors.primaryDarkest)
            }
        }

        if (uiState.isInsufficientPoints) {
            CustomSingleActionDialog(
                message = "컨텐츠를 시청하기 위한\n포인트가 부족해요!",
                subMessage = "매일 출석체크만 해도 5P를 받을 수 있어요!",
                buttonText = "배틀 주제 구경하러 가기",
                onDismiss = { onDismissPointDialog() },
                onConfirm = {
                    onDismissPointDialog()
                    onNavigateToExplore()
                }
            )
        }
    }
}

@Composable
private fun BattleNotFoundScreen(onBackClick: () -> Unit) {
    Scaffold(
        containerColor = PickeTheme.colors.backgroundBrand,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBrand,
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
                tint = PickeTheme.colors.borderDefault
            )
            Text(
                text = "해당 배틀은 존재하지 않습니다",
                style = PickeTheme.typography.b3Regular,
                color = PickeTheme.colors.beige800
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoteScreenPreview() {
    PickeTheme {
        VoteScreen(
            voteType = VoteType.PRE,
            battleDetail = DummyData.dummyBattleDetailList.first(),
            uiState = VoteUiState(),
            onBackClick = {},
            onVoteSubmit = {},
            onNavigateToExplore = {},
            onSubmitVote = { _, _ -> },
            onTrackShare = { },
            onGetShareLink = { _, _, _ -> },
            onDismissPointDialog = {}
        )
    }
}