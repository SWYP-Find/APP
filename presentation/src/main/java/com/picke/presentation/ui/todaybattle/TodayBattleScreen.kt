package com.picke.presentation.ui.todaybattle

import android.graphics.drawable.BitmapDrawable
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.SubcomposeAsyncImage
import coil.imageLoader
import coil.request.ImageRequest
import com.picke.presentation.R
import com.picke.presentation.analytics.ShareChannel
import com.picke.presentation.analytics.ShareTarget
import com.picke.presentation.analytics.rememberAnalyticsTracker
import com.picke.presentation.ui.component.CustomButton
import com.picke.presentation.ui.component.ShareDialog
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.ui.todaybattle.component.OpinionCard
import com.picke.presentation.ui.todaybattle.component.TodayBattleSkeleton
import com.picke.presentation.ui.todaybattle.component.TopIndicatorBar
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiModel
import com.picke.presentation.ui.todaybattle.model.TodayBattleUiState
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.shareBattleToInstagramStoryDarkMode
import com.picke.presentation.util.shareBattleToKakao
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun TodayBattleScreen(
    viewModel: TodayBattleViewModel = hiltViewModel(),
    initialBattleId: String? = null,
    onBackClick: () -> Unit,
    onNavigateToScenario: (String) -> Unit,
    onNavigateToPerspective: (String) -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val uiState by viewModel.uiState.collectAsState()

    val analyticsTracker = rememberAnalyticsTracker()

    LaunchedEffect(uiState.errorMessage) {
        uiState.errorMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    TodayBattleScreen(
        uiState = uiState,
        initialBattleId = initialBattleId,
        onBackClick = onBackClick,
        onEnterBattle = { currentBattleId, selectedOptionId ->
            viewModel.enterBattle(
                battleId = currentBattleId.toLong(),
                optionId = selectedOptionId!!.toLong(),
                onNavigateToScenario = onNavigateToScenario,
                onNavigateToPerspective = onNavigateToPerspective
            )
        },
        onGetShareLink = { currentBattleId ->
            viewModel.getShareLink(
                battleId = currentBattleId,
                onSuccess = { url ->
                    clipboardManager.setText(AnnotatedString(url))
                    Toast.makeText(context, "링크가 클립보드에 복사되었습니다.", Toast.LENGTH_SHORT).show()
                },
                onError = { errorMessage ->
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        },
        onTrackShareAction = { target, channel ->
            analyticsTracker.trackShareAction(target, channel)
        }
    )
}

@Composable
fun TodayBattleScreen(
    uiState: TodayBattleUiState,
    initialBattleId: String?,
    onBackClick: () -> Unit,
    onEnterBattle: (String, String?) -> Unit,
    onGetShareLink: (Int) -> Unit,
    onTrackShareAction: (String, String) -> Unit
) {
    val context = LocalContext.current

    val battleList = uiState.battleList

    val pagerState = rememberPagerState(pageCount = { battleList.size })
    val coroutineScope = rememberCoroutineScope()
    val graphicsLayer = rememberGraphicsLayer()

    var selectedOptionId by remember(pagerState.currentPage) { mutableStateOf<String?>(null) }
    var showShareDialog by remember { mutableStateOf(false) }
    var isSharing by remember { mutableStateOf(false) }

    val isButtonEnabled = selectedOptionId != null && !uiState.isEntering
    val currentBattle = if (battleList.isNotEmpty()) battleList[pagerState.currentPage] else null

    val onKakaoShareClick = {
        currentBattle?.let { battle ->
            isSharing = true
            coroutineScope.launch {
                try {
                    val request = ImageRequest.Builder(context)
                        .data(battle.imageUrl)
                        .allowHardware(false)
                        .build()
                    val result = context.imageLoader.execute(request)
                    val bitmap = (result.drawable as? BitmapDrawable)?.bitmap

                    if (bitmap != null) {
                        shareBattleToKakao(
                            context = context,
                            bitmap = bitmap,
                            battleId = battle.battleId,
                            battleTitle = battle.title,
                            battleDescription = battle.description,
                            onComplete = { isSharing = false }
                        )
                    } else {
                        isSharing = false
                        Toast.makeText(context, "이미지 로드 실패", Toast.LENGTH_SHORT).show()
                    }
                } catch (_: Exception) {
                    isSharing = false
                    Toast.makeText(context, "공유 실패", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    val onInstaShareClick = {
        isSharing = true
        coroutineScope.launch {
            try {
                delay(100.milliseconds)

                val bitmap = graphicsLayer.toImageBitmap().asAndroidBitmap()
                shareBattleToInstagramStoryDarkMode(
                    context = context,
                    bitmap = bitmap,
                    onComplete = { isSharing = false }
                )
            } catch (_: Exception) {
                isSharing = false
                Toast.makeText(context, "캡처 실패", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(battleList, initialBattleId) {
        if (initialBattleId != null && battleList.isNotEmpty()) {
            val targetPage = battleList.indexOfFirst { it.battleId == initialBattleId }
            if (targetPage >= 0) pagerState.animateScrollToPage(targetPage)
        }
    }

    if (uiState.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            TodayBattleSkeleton(modifier = Modifier.fillMaxSize())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(40.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "뒤로가기",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        return
    }

    if (battleList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.size(20.dp)) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_arrow_left),
                        contentDescription = "뒤로가기",
                        tint = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
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
                    text = "아직 빠른 배틀이 선정되지 않았어요\n조금만 기다려주세요!",
                    style = PickeTheme.typography.b3Regular,
                    color = PickeTheme.colors.surfaceTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }

        return
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Black,
            bottomBar = {
                CustomButton(
                    text = stringResource(R.string.battle_start),
                    onClick = {
                        if (isButtonEnabled) {
                            val currentBattleId = battleList[pagerState.currentPage].battleId
                            onEnterBattle(currentBattleId, selectedOptionId)
                        }
                    },
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(20.dp),
                    backgroundColor = if (isButtonEnabled) PickeTheme.colors.primary else PickeTheme.colors.primaryDisabled,
                    textColor = PickeTheme.colors.surfaceDefault
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = innerPadding.calculateBottomPadding())
                    .background(Color.Black)
                    .drawWithCache {
                        onDrawWithContent {
                            graphicsLayer.record {
                                this@onDrawWithContent.drawContent()
                            }
                            drawLayer(graphicsLayer)
                        }
                    }
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    BattleContent(
                        item = battleList[page],
                        selectedOptionId = selectedOptionId,
                        onOptionSelect = { optionId -> selectedOptionId = optionId }
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    if (battleList.size > 1) {
                        TopIndicatorBar(
                            currentPage = pagerState.currentPage,
                            totalPages = battleList.size
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_left),
                                contentDescription = "뒤로가기",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = { showShareDialog = true },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_share),
                                contentDescription = "공유",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }

        if (showShareDialog) {
            ShareDialog(
                onDismiss = { showShareDialog = false },
                onKakaoClick = {
                    showShareDialog = false
                    onTrackShareAction(ShareTarget.BATTLE, ShareChannel.KAKAO)
                    onKakaoShareClick()
                },
                onInstaClick = {
                    showShareDialog = false
                    onTrackShareAction(ShareTarget.BATTLE, ShareChannel.INSTAGRAM)
                    onInstaShareClick()
                },
                onFacebookClick = {
                    showShareDialog = false
                },
                onCopyLinkClick = {
                    showShareDialog = false
                    onTrackShareAction(ShareTarget.BATTLE, ShareChannel.LINK)

                    val currentBattleId = battleList[pagerState.currentPage].battleId.toInt()
                    onGetShareLink(currentBattleId)
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
    }
}

@Composable
fun BattleContent(
    item: TodayBattleUiModel,
    selectedOptionId: String?,
    onOptionSelect: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            SubcomposeAsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Black.copy(alpha = 0.2f),
                                        Color.Black.copy(alpha = 0.6f),
                                        Color.Black
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
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer(PickeTheme.colors.neutral600, PickeTheme.colors.neutral400)
                    )
                }
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item.tags.forEach { tag ->
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text = "#$tag",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = PickeTheme.typography.label,
                                color = PickeTheme.colors.primary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = item.title,
                    style = PickeTheme.typography.h1SemiBold,
                    color = PickeTheme.colors.surface,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.description,
                    style = PickeTheme.typography.b3Regular,
                    color = PickeTheme.colors.neutral400,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = Color.Transparent,
                    shape = RoundedCornerShape(2.dp),
                    border = BorderStroke(1.dp, PickeTheme.colors.textSecondary)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_clock),
                            contentDescription = null,
                            modifier = Modifier.size(12.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            item.timeLeft,
                            style = PickeTheme.typography.labelXSmall,
                            color = Color.LightGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (item.options.isNotEmpty()) {
                    val optionA = item.options[0]
                    OpinionCard(
                        name = optionA.name,
                        opinion = optionA.opinion,
                        quote = optionA.quote,
                        isSelected = selectedOptionId == optionA.optionId,
                        onClick = { onOptionSelect(optionA.optionId) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                if (item.options.size > 1) {
                    val optionB = item.options[1]
                    OpinionCard(
                        name = optionB.name,
                        opinion = optionB.opinion,
                        quote = optionB.quote,
                        isSelected = selectedOptionId == optionB.optionId,
                        onClick = { onOptionSelect(optionB.optionId) }
                    )
                }
            }

            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = PickeTheme.colors.secondaryLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("VS", style = PickeTheme.typography.b3SemiBold, color = Color.Black)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun TodayBattleScreenPreview() {
    PickeTheme {
        TodayBattleScreen(
            uiState = TodayBattleUiState(
                battleList = DummyData.dummyTodayBattles
            ),
            initialBattleId = "",
            onBackClick = { },
            onEnterBattle = { _, _ -> },
            onGetShareLink = { },
            onTrackShareAction = { _, _ -> }
        )
    }
}