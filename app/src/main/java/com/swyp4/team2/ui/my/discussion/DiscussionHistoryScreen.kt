package com.swyp4.team2.ui.my.discussion

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.my.discussion.model.DiscussionHistoryItem
import com.swyp4.team2.ui.my.discussion.model.dummyAgreeList
import com.swyp4.team2.ui.my.discussion.model.dummyDisagreeList
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun DiscussionHistoryScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("찬성", "반대")

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = SwypTheme.colors.surface,
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_discussion),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = {onBackClick()},
                backgroundColor = SwypTheme.colors.surface
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

            // 페이지 내용
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ){ page ->
                when (page){
                    0 -> AgreeDiscussionList()
                    1 -> DisagreeDiscussionList()
                }
            }
        }
    }
}

@Composable
fun AgreeDiscussionList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dummyAgreeList) { item ->
            DiscussionHistoryCard(item = item)
        }
    }
}

@Composable
fun DisagreeDiscussionList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dummyDisagreeList) { item ->
            DiscussionHistoryCard(item = item)
        }
    }
}

@Composable
fun DiscussionHistoryCard(
    item: DiscussionHistoryItem
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        // [상단] 카테고리 뱃지 & 토론 주제
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 카테고리 뱃지
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(Beige400)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "#${item.category}",
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary
                )
            }

            // 토론 주제
            Text(
                text = item.title,
                style = SwypTheme.typography.labelMedium,
                color = Gray900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // [중단] 내가 쓴 댓글 내용
        Text(
            text = item.description,
            style = SwypTheme.typography.b4Regular,
            color = Gray700,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(16.dp))

        // [하단] 작성 날짜
        Text(
            text = item.date,
            style = SwypTheme.typography.label,
            color = Gray500
        )
    }
}