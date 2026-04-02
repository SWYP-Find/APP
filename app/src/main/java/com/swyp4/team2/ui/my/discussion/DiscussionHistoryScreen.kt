package com.swyp4.team2.ui.my.discussion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.MyBattleRecordItem
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige800
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun DiscussionHistoryScreen(
    onBackClick: () -> Unit,
    // onNavigateToComment: (Long) -> Unit,
    onNavigateToDetail: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: DiscussionHistoryViewModel = hiltViewModel()
) {
    val tabs = listOf("찬성", "반대")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Beige200,
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_discussion),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = {onBackClick()},
                backgroundColor = Beige200
            )
        }
    ){ innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ){
            // 탭바
            CustomTabBar(
                tabs = tabs,
                selectedTab = tabs[pagerState.currentPage],
                isScrollable = false,
                onTabSelected = { selectedTab ->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(tabs.indexOf(selectedTab))
                    }
                }
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
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
                        0 -> DiscussionHistoryList(
                            list = uiState.agreeList,
                            voteSide = "PRO",
                            emptyMessage = "아직 찬성 의견을 남긴 토론이 없습니다",
                            onItemClick = onNavigateToDetail,
                            onLoadMore = { viewModel.loadMore("PRO") }
                        )
                        1 -> DiscussionHistoryList(
                            list = uiState.disagreeList,
                            voteSide = "CON",
                            emptyMessage = "아직 반대 의견을 남긴 토론이 없습니다",
                            onItemClick = onNavigateToDetail,
                            onLoadMore = { viewModel.loadMore("CON") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DiscussionHistoryList(
    list: List<MyBattleRecordItem>,
    voteSide: String,
    emptyMessage: String,
    onItemClick: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    if (list.isEmpty()) {
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
            itemsIndexed(list) { index, item ->
                // 리스트 끝에서 2번째 아이템에 도달하면 다음 페이지 데이터 호출
                if (index >= list.size - 2) {
                    onLoadMore()
                }

                DiscussionHistoryCard(
                    item = item,
                    onClick = { onItemClick(item.battleId) }
                )
            }
        }
    }
}

@Composable
fun DiscussionHistoryCard(
    item: MyBattleRecordItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // [상단] 토론 주제
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Surface(
                color = Beige600,
                shape = RoundedCornerShape(2.dp)
            ) {
                Text(
                    text = "#${item.category ?: "이슈"}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary
                )
            }
            Text(
                text = item.title,
                style = SwypTheme.typography.labelMedium,
                color = Gray500,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // [중단] 내가 남긴 요약 내용
        Text(
            text = item.summary,
            style = SwypTheme.typography.b4Regular,
            color = Gray400,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        // [하단] 작성 날짜
        Text(
            text = item.createdAt,
            style = SwypTheme.typography.label,
            color = Gray200
        )
    }
}