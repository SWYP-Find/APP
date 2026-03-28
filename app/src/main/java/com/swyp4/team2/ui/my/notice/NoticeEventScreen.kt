package com.swyp4.team2.ui.my.notice

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.NoticeEventItem
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige800
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun NoticeEventScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NoticeEventViewModel = hiltViewModel()
) {
    val tabs = listOf("공지사항", "이벤트")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    var selectedItem by remember { mutableStateOf<NoticeEventItem?>(null) }

    BackHandler(enabled = selectedItem != null) {
        selectedItem = null
    }

    Scaffold(
        containerColor = Beige200,
        topBar = {
            CustomTopAppBar(
                title = tabs[pagerState.currentPage],
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = {
                    if (selectedItem != null) {
                        selectedItem = null
                    } else {
                        onBackClick()
                    }
                },
                backgroundColor = Beige200
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
            CustomTabBar(
                tabs = tabs,
                selectedTab = tabs[pagerState.currentPage],
                isScrollable = false,
                onTabSelected = { selectedTab ->
                    val targetPage = tabs.indexOf(selectedTab)

                    if (selectedItem != null) {
                        selectedItem = null
                    }

                    coroutineScope.launch {
                        pagerState.animateScrollToPage(targetPage)
                    }
                }
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary900)
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> {
                            if (selectedItem != null && selectedItem?.type == "공지사항") {
                                NoticeEventDetailContent(
                                    item = selectedItem!!,
                                    onGoToList = { selectedItem = null }
                                )
                            } else {
                                NoticeEventList(
                                    items = uiState.noticeList,
                                    emptyMessage = "아직 등록된 공지사항이 없습니다",
                                    onItemClick = { item -> selectedItem = item }
                                )
                            }
                        }
                        1 -> {
                            if (selectedItem != null && selectedItem?.type == "이벤트") {
                                NoticeEventDetailContent(
                                    item = selectedItem!!,
                                    onGoToList = { selectedItem = null }
                                )
                            } else {
                                NoticeEventList(
                                    items = uiState.eventList,
                                    emptyMessage = "아직 진행 중인 이벤트가 없습니다",
                                    onItemClick = { item -> selectedItem = item }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoticeEventDetailContent(
    item: NoticeEventItem,
    onGoToList: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SwypTheme.colors.surface)
                .padding(horizontal = 24.dp, vertical = 32.dp)
        ) {
            // [뱃지]
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(Beige600)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = item.type,
                    style = SwypTheme.typography.b5Medium,
                    color = Primary500
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // [제목]
            Text(
                text = item.title,
                style = SwypTheme.typography.labelMedium,
                color = Gray500
            )

            Spacer(modifier = Modifier.height(8.dp))

            // [날짜]
            Text(
                text = item.date,
                style = SwypTheme.typography.b5Medium,
                color = Gray300
            )

            Spacer(modifier = Modifier.height(24.dp))

            // [본문] (Domain 모델에 content 필드를 꼭 추가해주세요!)
            Text(
                text = item.content,
                style = SwypTheme.typography.b4Regular,
                color = Gray400
            )

            Spacer(modifier = Modifier.height(48.dp))

            // [목록 버튼]
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(2.dp))
                        .background(Primary500)
                        .clickable { onGoToList() }
                        .padding(horizontal = 32.dp, vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "목록",
                        style = SwypTheme.typography.b3SemiBold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun NoticeEventList(
    items: List<NoticeEventItem>,
    emptyMessage: String,
    onItemClick: (NoticeEventItem) -> Unit
) {
    if (items.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logo),
                contentDescription = "빈 화면 로고",
                modifier = Modifier.size(width = 160.dp, height = 120.dp),
                tint = Beige600
            )
            Text(
                text = emptyMessage,
                style = SwypTheme.typography.b3Regular,
                color = Beige800
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(items) { item ->
                NoticeEventCard(
                    item = item,
                    onClick = { onItemClick(item) }
                )
            }
        }
    }
}

@Composable
fun NoticeEventCard(
    item: NoticeEventItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige400, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // [상단] 뱃지 (공지사항 / 이벤트)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .background(Beige600)
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = item.type,
                style = SwypTheme.typography.b5Medium,
                color = Primary500
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // [중단] 제목
        Text(
            text = item.title,
            style = SwypTheme.typography.labelMedium,
            color = Gray500,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // [하단] 날짜
        Text(
            text = item.date,
            style = SwypTheme.typography.b5Medium,
            color = Gray300
        )
    }
}