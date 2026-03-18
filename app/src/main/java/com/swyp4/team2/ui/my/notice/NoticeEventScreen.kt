package com.swyp4.team2.ui.my.notice

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.my.notice.model.NoticeEventItem
import com.swyp4.team2.ui.my.notice.model.dummyEventList
import com.swyp4.team2.ui.my.notice.model.dummyNoticeList
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun NoticeEventScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("공지사항", "이벤트")

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            CustomTopAppBar(
                title = tabs[pagerState.currentPage],
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = SwypTheme.colors.background
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
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(targetPage)
                    }
                }
            )

            // 페이지 내용
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                when (page) {
                    0 -> NoticeEventList(items = dummyNoticeList)
                    1 -> NoticeEventList(items = dummyEventList)
                }
            }
        }
    }
}

@Composable
fun NoticeEventList(items: List<NoticeEventItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            NoticeEventCard(item = item)
        }
    }
}

@Composable
fun NoticeEventCard(item: NoticeEventItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige400, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        // [상단] 뱃지 (공지사항 / 이벤트)
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .background(Beige400) // 연한 베이지색 배경
                .padding(horizontal = 6.dp, vertical = 2.dp)
        ) {
            Text(
                text = item.type,
                style = SwypTheme.typography.labelXSmall,
                color = SwypTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // [중단] 제목
        Text(
            text = item.title,
            style = SwypTheme.typography.b2Medium,
            color = Gray900,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // [하단] 날짜
        Text(
            text = item.date,
            style = SwypTheme.typography.labelXSmall,
            color = Gray400
        )
    }
}