package com.swyp4.team2.ui.my.content

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.my.content.model.MyCommentItem
import com.swyp4.team2.ui.my.content.model.MyLikeItem
import com.swyp4.team2.ui.my.content.model.dummyCommentList
import com.swyp4.team2.ui.my.content.model.dummyLikeList
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary100
import com.swyp4.team2.ui.theme.Primary50
import com.swyp4.team2.ui.theme.Secondary900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun ContentActivityScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val tabs = listOf("댓글", "좋아요")

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_content),
                centerTitle = true,
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
            ){ page ->
                when (page){
                    0 -> MyCommentList()
                    1 -> MyLikeList()
                }
            }
        }
    }
}

@Composable
fun MyCommentList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(dummyCommentList){ item ->
            MyCommentCard(item = item)
        }
    }
}

@Composable
fun MyLikeList() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dummyLikeList) { item ->
            MyLikeCard(item = item)
        }
    }
}

@Composable
fun MyCommentCard(item: MyCommentItem) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige400, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = item.category,
                style = SwypTheme.typography.h5SemiBold,
                color = SwypTheme.colors.primary
            )
            Text(
                text = " | ",
                style = SwypTheme.typography.labelMedium,
                color = SwypTheme.colors.primary
            )
            Text(
                text = item.title,
                style = SwypTheme.typography.labelMedium,
                color = Gray900
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = item.content,
            style = SwypTheme.typography.b4Regular,
            color = Gray600,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.date,
                style = SwypTheme.typography.label,
                color = Gray300
            )
            Text(
                text = stringResource(R.string.my_content_view_detail),
                style = SwypTheme.typography.label,
                color = Gray400
            )
        }
    }
}

@Composable
fun MyLikeCard(item: MyLikeItem) {
    Column(
        modifier = Modifier.fillMaxSize()
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige400, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ){
        // 상단 정보들
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            // 프로필, 닉네임, 찬반, 시간
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(36.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Beige500),
                    contentAlignment = Alignment.Center
                ) {

                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.nickname,
                            style = SwypTheme.typography.labelMedium,
                            color = Gray900
                        )
                        Text(
                            text = " | ",
                            style = SwypTheme.typography.label,
                            color = Secondary900
                        )
                        Text(
                            text = item.stance,
                            style = SwypTheme.typography.label,
                            color = Secondary900
                        )
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.timeAgo,
                        style = SwypTheme.typography.labelXSmall,
                        color = Gray400,
                    )
                }
            }

            // 좋아요 버튼
            Row(
                modifier = Modifier.clip(RoundedCornerShape(2.dp))
                    .background(Primary50)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_plus),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Gray400
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = item.likeCount,
                    style = SwypTheme.typography.label,
                    color = Gray400
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 본문
        Text(
            text = item.content,
            style = SwypTheme.typography.b4Regular,
            color = Gray600,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
    }
}