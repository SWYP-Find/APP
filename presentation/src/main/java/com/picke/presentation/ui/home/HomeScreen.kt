package com.picke.presentation.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import coil.compose.SubcomposeAsyncImage
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.attendance.AttendanceCheckBottomSheet
import com.picke.presentation.ui.component.AdFitBannerAd
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.home.component.BestBattleRankItem
import com.picke.presentation.ui.home.component.HomeSkeleton
import com.picke.presentation.ui.home.component.NewBattleCard
import com.picke.presentation.ui.home.component.TrendingBattleCard
import com.picke.presentation.ui.home.model.HomeContentUiModel
import com.picke.presentation.ui.home.model.HomeUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import com.picke.presentation.util.showAdFitTransitionPopupAd
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAlarm: () -> Unit,
    onNavigateToVote: (String) -> Unit,
    onNavigateToTrendingBattle: () -> Unit,
    onNavigateToBestBattle: () -> Unit,
    onNavigateToTodayPicke: () -> Unit,
    onNavigateToNewBattle: () -> Unit,
    scrollToTopTrigger: Int = 0,
    isNotificationSheetPending: Boolean = false,
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(isNotificationSheetPending) {
        if (!isNotificationSheetPending) {
            viewModel.checkInAndShowAttendanceSheetIfNeeded()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.fetchUnreadAlarmStatus()
    }

    LaunchedEffect(Unit) {
        (context as? FragmentActivity)?.let { activity ->
            showAdFitTransitionPopupAd(activity, BuildConfig.ADFIT_APP_TRANSITION)
        }
    }

    HomeScreen(
        uiState = uiState,
        onNavigateToAlarm = onNavigateToAlarm,
        onNavigateToVote = onNavigateToVote,
        onNavigateToTrendingBattle = onNavigateToTrendingBattle,
        onNavigateToBestBattle = onNavigateToBestBattle,
        onNavigateToTodayPicke = onNavigateToTodayPicke,
        onNavigateToNewBattle = onNavigateToNewBattle,
        onFetchHomeData = { viewModel.fetchHomeData() },
        onDismissAttendanceCheckSheet = { viewModel.dismissAttendanceCheckSheet() },
        scrollToTopTrigger = scrollToTopTrigger
    )
}

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onNavigateToAlarm: () -> Unit,
    onNavigateToVote: (String) -> Unit,
    onNavigateToTrendingBattle: () -> Unit,
    onNavigateToBestBattle: () -> Unit,
    onNavigateToTodayPicke: () -> Unit,
    onNavigateToNewBattle: () -> Unit,
    onFetchHomeData: () -> Unit,
    onDismissAttendanceCheckSheet: () -> Unit,
    scrollToTopTrigger: Int,
) {
    val scrollState = rememberScrollState()

    val isDataEmpty = uiState.editorPicks.isEmpty() &&
            uiState.trendingBattles.isEmpty() &&
            uiState.bestBattles.isEmpty() &&
            uiState.newBattles.isEmpty()

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            scrollState.animateScrollTo(0)
            onFetchHomeData()
        }
    }

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBrand,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = PickeTheme.colors.backgroundBrand,
                actions = {
                    if (uiState.isLoading || uiState.isAlarmStatusLoading) {
                        Box(
                            modifier = Modifier.size(48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Spacer(
                                modifier = Modifier
                                    .size(24.dp)
                                    .clip(CircleShape)
                                    .shimmer()
                            )
                        }
                    } else {
                        IconButton(onClick = onNavigateToAlarm) {
                            BadgedBox(
                                badge = {
                                    if (uiState.hasNewNotice) {
                                        Badge(
                                            containerColor = PickeTheme.colors.primary,
                                            modifier = Modifier.offset(x = 4.dp, y = (-4).dp)
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_alarm),
                                    contentDescription = "알림",
                                    tint = Color.Unspecified
                                )
                            }
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            HomeSkeleton(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .verticalScroll(scrollState)
            )
        } else if (isDataEmpty) {
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "아직 준비된 배틀이 없어요!",
                    style = PickeTheme.typography.b3Regular,
                    color = PickeTheme.colors.beige800
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .verticalScroll(scrollState)
            ) {
                if (uiState.editorPicks.isNotEmpty()) {
                    EditorPickSection(
                        items = uiState.editorPicks,
                        onItemClick = { contentId ->
                            onNavigateToVote(contentId)
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                if (uiState.trendingBattles.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                            HomeSectionHeader(
                                title = stringResource(R.string.home_section_trending),
                                highlightText = stringResource(R.string.home_highlight_battle),
                                onMoreClick = onNavigateToTrendingBattle
                            )
                        }
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.trendingBattles) { item ->
                                TrendingBattleCard(
                                    item = item,
                                    onClick = { onNavigateToVote(item.contentId) }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_320X100)
                }

                Spacer(modifier = Modifier.height(24.dp))
                if (uiState.bestBattles.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        HomeSectionHeader(
                            title = stringResource(R.string.home_section_best),
                            highlightText = stringResource(R.string.home_highlight_battle),
                            onMoreClick = onNavigateToBestBattle
                        )

                        uiState.bestBattles.take(3).forEachIndexed { index, item ->
                            BestBattleRankItem(
                                item = item,
                                rank = index + 1,
                                onClick = { onNavigateToVote(item.contentId) }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }

                // 4. 오늘의 Pické (투표/퀴즈)
                //  - 홈에서 오늘의 Pické 섹션을 노출하지 않기로 하여 전체 주석 처리.
                //  - 백엔드에서 데이터를 내려주더라도 홈에서는 그리지 않는다. 필요 시 아래 블록을 복원하면 된다.
                /*
                if (uiState.todayPicks.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        HomeSectionHeader(
                            title = stringResource(R.string.home_section_today_picke),
                            highlightText = stringResource(R.string.home_highlight_picke),
                            onMoreClick = onNavigateToTodayPicke
                        )
                        uiState.todayPicks.forEach { item ->
                            TodayPickeCard(
                                item = item,
                                onVoteClick = { optionId ->
                                    viewModel.submitTodayPickVote(
                                        battleId = item.contentId,
                                        optionId = optionId,
                                        type = item.type
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
                */

                if (uiState.newBattles.isNotEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        HomeSectionHeader(
                            title = stringResource(R.string.home_section_new),
                            highlightText = stringResource(R.string.home_highlight_battle),
                            onMoreClick = onNavigateToNewBattle
                        )
                        uiState.newBattles.forEach { item ->
                            NewBattleCard(
                                item = item,
                                onClick = { onNavigateToVote(item.contentId) }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))

            }
        }
    }

    uiState.attendanceCheckUiState?.let { attendanceCheckUiState ->
        AttendanceCheckBottomSheet(
            uiState = attendanceCheckUiState,
            onDismiss = onDismissAttendanceCheckSheet
        )
    }
}

@Composable
fun HomeSectionHeader(
    title: String,
    highlightText: String? = null,
    onMoreClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val annotatedTitle = buildAnnotatedString {
            if (highlightText != null && title.contains(highlightText)) {
                val startIndex = title.indexOf(highlightText)
                val endIndex = startIndex + highlightText.length
                append(title.substring(0, startIndex))
                withStyle(style = SpanStyle(color = PickeTheme.colors.primary)) {
                    append(
                        highlightText
                    )
                }
                append(title.substring(endIndex))
            } else {
                append(title)
            }
        }

        Text(
            text = annotatedTitle,
            style = PickeTheme.typography.h3SemiBold,
            color = PickeTheme.colors.textPrimary
        )
        /*Text(
            text = stringResource(R.string.more),
            style = SwypTheme.typography.b4Medium,
            color = SwypTheme.colors.textTertiary,
            modifier = Modifier.clickable { onMoreClick() }
        )*/
    }
}

@Composable
fun EditorPickSection(
    items: List<HomeContentUiModel>,
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit
) {
    if (items.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { items.size })

    LaunchedEffect(pagerState, items.size) {
        if (items.size <= 1) return@LaunchedEffect
        while (true) {
            delay(3000.milliseconds)
            val nextPage = (pagerState.currentPage + 1) % items.size
            pagerState.animateScrollToPage(
                page = nextPage,
                animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
            )
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(color = PickeTheme.colors.primary, shape = RoundedCornerShape(2.dp)) {
                Text(
                    text = "EDITOR PICK",
                    style = PickeTheme.typography.caption2SemiBold,
                    color = PickeTheme.colors.secondaryLight,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }

            Surface(
                color = PickeTheme.colors.textTertiary.copy(alpha = 0.8f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        ) {
                            append((pagerState.currentPage + 1).toString())
                        }
                        withStyle(
                            style = SpanStyle(
                                color = PickeTheme.colors.surfaceDefault.copy(
                                    alpha = 0.6f
                                )
                            )
                        ) {
                            append("/${items.size}")
                        }
                    },
                    style = PickeTheme.typography.labelXSmall,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth()
        ) { page ->
            val pagerItem = items[page]

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onItemClick(pagerItem.contentId) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.8f)
                ) {
                    SubcomposeAsyncImage(
                        model = pagerItem.thumbnailUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        loading = {
                            Spacer(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .shimmer(
                                        PickeTheme.colors.neutral600,
                                        PickeTheme.colors.neutral400
                                    )
                            )
                        }
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )

                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        val textStyle = PickeTheme.typography.h4SemiBold.copy(
                            shadow = Shadow(
                                color = Color.Black,
                                offset = Offset(2f, 2f),
                                blurRadius = 4f
                            )
                        )
                        Text(
                            text = pagerItem.leftOpinion ?: "A",
                            style = textStyle,
                            color = Color.White
                        )
                        Icon(
                            painter = painterResource(R.drawable.ic_versus),
                            contentDescription = null,
                            tint = Color.White
                        )
                        Text(
                            text = pagerItem.rightOpinion ?: "B",
                            style = textStyle,
                            color = Color.White
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = pagerItem.title,
                        style = PickeTheme.typography.h4SemiBold,
                        color = Color.White,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = pagerItem.summary,
                        style = PickeTheme.typography.label,
                        color = PickeTheme.colors.neutral400,
                        minLines = 2,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = PickeTheme.typography.label.fontSize * 1.4
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = pagerItem.tags.joinToString(" ") { "#$it" },
                            style = PickeTheme.typography.label,
                            color = PickeTheme.colors.textMuted
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_eye),
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = PickeTheme.colors.neutral400
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = pagerItem.viewCountText,
                                style = PickeTheme.typography.label,
                                color = PickeTheme.colors.textMuted
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        uiState = HomeUiState(
            editorPicks = DummyData.dummyHomeContentItems,
            trendingBattles = DummyData.dummyHomeContentItems,
            bestBattles = DummyData.dummyHomeContentItems,
            newBattles = DummyData.dummyHomeContentItems,
            todayPicks = listOf(DummyData.dummyVotePick, DummyData.dummyQuizPick)
        ),
        onNavigateToAlarm = {},
        onNavigateToVote = {},
        onNavigateToTrendingBattle = {},
        onNavigateToBestBattle = {},
        onNavigateToTodayPicke = {},
        onNavigateToNewBattle = {},
        onFetchHomeData = {},
        onDismissAttendanceCheckSheet = {},
        scrollToTopTrigger = 0
    )
}