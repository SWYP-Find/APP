package com.swyp4.team2.ui.home

import android.graphics.Picture
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.draw
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.component.ProfileImage
import com.swyp4.team2.ui.home.model.BattleProfile
import com.swyp4.team2.ui.home.model.BestBattleItem
import com.swyp4.team2.ui.home.model.EditorPickItem
import com.swyp4.team2.ui.home.model.NewBattleItem
import com.swyp4.team2.ui.home.model.TodayPickeItem
import com.swyp4.team2.ui.home.model.TrendingBattleItem
import com.swyp4.team2.ui.home.model.dummyBestBattleList
import com.swyp4.team2.ui.home.model.dummyEditorPickList
import com.swyp4.team2.ui.home.model.dummyNewBattleList
import com.swyp4.team2.ui.home.model.dummyPickeList
import com.swyp4.team2.ui.home.model.dummyTrendingList
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige300
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige50
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige700
import com.swyp4.team2.ui.theme.Beige800
import com.swyp4.team2.ui.theme.Beige900
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary200
import com.swyp4.team2.ui.theme.SwypAppTheme
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAlarm: ()->Unit,
    onNavigateToVote: (String) -> Unit,
    onNavigateToTrendingBattle : ()->Unit,
    onNavigateToBestBattle : ()->Unit,
    onNavigateToTodayPicke : ()->Unit,
    onNavigateToNewBattle : ()->Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()

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
            Box(modifier = Modifier.fillMaxSize())
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(SwypTheme.colors.surface)
                .verticalScroll(scrollState)
        ){
            // 1. 에디터 픽 섹션
            if (uiState.editorPicks.isNotEmpty()) {
                EditorPickSection(
                    items = uiState.editorPicks,
                    onItemClick = onNavigateToVote
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
                            rank = index + 1, // 🌟 1, 2, 3 순위 계산
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
                        TodayPickeCard(item = item)
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