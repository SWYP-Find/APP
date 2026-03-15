package com.swyp4.team2.ui.my.history

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
import com.swyp4.team2.ui.my.content.MyCommentList
import com.swyp4.team2.ui.my.content.MyLikeList
import com.swyp4.team2.ui.my.history.model.DiscussionHistoryItem
import com.swyp4.team2.ui.my.history.model.dummyAgreeList
import com.swyp4.team2.ui.my.history.model.dummyDisagreeList
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray50
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Secondary900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.CoroutineScope
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
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_discussion),
                showLogo = false,
                showBackButton = true,
                onBackClick = {onBackClick()},
                backgroundColor = SwypTheme.colors.background
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
    Row(
        modifier = Modifier.fillMaxWidth()
            .background(SwypTheme.colors.surface)
            .clip(RoundedCornerShape(2.dp))
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 제목, 태그들
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = SwypTheme.typography.labelMedium,
                color = Gray900,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Beige400)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.category,
                        style = SwypTheme.typography.b4Regular,
                        color = Secondary900
                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                // 시간 태그
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Beige400)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.timeAgo,
                        style = SwypTheme.typography.b4Regular,
                        color = Secondary900
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        // 찬성/반대 뱃지
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(2.dp))
                .background(Beige600)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = item.stance,
                style = SwypTheme.typography.h5SemiBold,
                color = SwypTheme.colors.primary
            )
        }
    }
}