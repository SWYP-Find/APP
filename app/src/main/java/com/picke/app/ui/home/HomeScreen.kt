package com.picke.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import com.picke.app.BuildConfig
import com.picke.app.R
import com.picke.app.ui.attendance.AttendanceCheckBottomSheet
import com.picke.app.ui.component.AdFitBannerAd
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.shimmer
import com.picke.app.ui.theme.SwypTheme
import com.picke.app.util.showAdFitTransitionPopupAd

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAlarm: ()->Unit,
    onNavigateToVote: (String) -> Unit,
    onNavigateToTrendingBattle : ()->Unit,
    onNavigateToBestBattle : ()->Unit,
    onNavigateToTodayPicke : ()->Unit,
    onNavigateToNewBattle : ()->Unit,
    scrollToTopTrigger: Int = 0,
    isNotificationSheetPending: Boolean = false,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    // 오늘의 Pické(투표/퀴즈) 섹션은 홈에서 제거하기로 하여 빈 화면 판정에서도 제외한다.
    val isDataEmpty = uiState.editorPicks.isEmpty() &&
            uiState.trendingBattles.isEmpty() &&
            uiState.bestBattles.isEmpty() &&
            uiState.newBattles.isEmpty()

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            scrollState.animateScrollTo(0)
            viewModel.fetchHomeData()
        }
    }

    // 신규 가입 유저는 알림 권한 바텀시트가 먼저 떠야 하므로, 그 시트가 처리되기 전까지는
    // 출석체크 바텀시트를 띄우지 않는다. 기존 유저는 처음부터 false이므로 즉시 진행된다.
    LaunchedEffect(isNotificationSheetPending) {
        if (!isNotificationSheetPending) {
            viewModel.checkInAndShowAttendanceSheetIfNeeded()
        }
    }

    // 최초 진입/탭 복귀/알림함에서 돌아올 때마다 미읽음 알림 여부를 조회해 벨 아이콘 배지를 갱신한다.
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.fetchUnreadAlarmStatus()
    }

    // 홈 화면 진입 시 카카오 애드핏 앱 전환(팝업) 광고 노출 시도.
    // AdFit 자체 빈도 제한/오늘 그만보기 정책이 있어 매번 뜨지는 않는다.
    LaunchedEffect(Unit) {
        (context as? FragmentActivity)?.let { activity ->
            showAdFitTransitionPopupAd(activity, BuildConfig.ADFIT_APP_TRANSITION)
        }
    }

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = SwypTheme.colors.backgroundBrand,
                actions = {
                    // 벨 배지(미읽음 여부)는 API 응답 후에야 확정되므로,
                    // 그 전까지는 아이콘 자리도 스켈레톤과 동일하게 shimmer로 보여준다.
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
                        // 배지는 서버의 미읽음 여부 응답으로만 갱신한다.
                        // (여기서 임의로 숨기면 알림함에서 돌아올 때 배지가 다시 나타나는 깜빡임이 생긴다)
                        IconButton(onClick = onNavigateToAlarm) {
                            BadgedBox(
                                badge = {
                                    if (uiState.hasNewNotice) {
                                        Badge(
                                            containerColor = SwypTheme.colors.primary,
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
    ){ innerPadding ->
        // 1. 데이터 로딩중
        if (uiState.isLoading) {
            HomeSkeleton(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .verticalScroll(scrollState)
            )
        }
        // 2. 데이터가 없을떄
        else if (isDataEmpty) {
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "아직 준비된 배틀이 없어요!",
                    style = SwypTheme.typography.b3Regular,
                    color = SwypTheme.colors.beige800
                )
            }
        }
        // 3. 데이터 있을때
        else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding())
                    .verticalScroll(scrollState)
            ) {
                // 1. 에디터 픽 섹션
                if (uiState.editorPicks.isNotEmpty()) {
                    EditorPickSection(
                        items = uiState.editorPicks,
                        onItemClick = { contentId ->
                            onNavigateToVote(contentId)
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }

                // 2. 지금 뜨는 배틀
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

                // 카카오 애드핏 배너 광고 (지금 뜨는 배틀 ↔ Best 배틀 사이)
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_320X100)
                }
                Spacer(modifier = Modifier.height(24.dp))

                // 3. Best 배틀
                if (uiState.bestBattles.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                        HomeSectionHeader(
                            title = stringResource(R.string.home_section_best),
                            highlightText = stringResource(R.string.home_highlight_battle),
                            onMoreClick = onNavigateToBestBattle
                        )
                        // Best는 상위 3개만 자르고 index를 넘겨 순위를 표시합니다.
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

                // 5. 새로운 배틀
                if (uiState.newBattles.isNotEmpty()) {
                    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
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
            onDismiss = { viewModel.dismissAttendanceCheckSheet() }
        )
    }
}