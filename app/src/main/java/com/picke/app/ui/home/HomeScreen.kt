package com.picke.app.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.picke.app.R
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Primary900
import com.picke.app.ui.theme.SwypTheme

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
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            scrollState.animateScrollTo(0)
            viewModel.fetchHomeData()
        }
    }

    Scaffold(
        containerColor = Beige200,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = Beige200,
                actions = {
                    IconButton(onClick = { onNavigateToAlarm() }) {
                        BadgedBox(
                            badge = {
                                if (uiState.hasNewNotice) {
                                    Badge(containerColor = SwypTheme.colors.primary)
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
            )
        }
    ){ innerPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        } else {
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

                // 4. 오늘의 Pické
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
}