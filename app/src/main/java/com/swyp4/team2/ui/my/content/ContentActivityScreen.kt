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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.ProfileImage
import com.swyp4.team2.ui.my.content.model.ContentActivityItem
import com.swyp4.team2.ui.my.content.model.dummyCommentList
import com.swyp4.team2.ui.my.content.model.dummyLikeList
import com.swyp4.team2.ui.theme.Beige400
import com.swyp4.team2.ui.theme.Beige500
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
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
    val tabs = listOf("내 댓글", "좋아요")

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = SwypTheme.colors.surface,
        topBar={
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_content),
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
                    0 -> ContentActivityList(items = dummyCommentList)
                    1 -> ContentActivityList(items = dummyLikeList)
                }
            }
        }
    }
}

@Composable
fun ContentActivityList(items: List<ContentActivityItem>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            ContentActivityCard(item = item)
        }
    }
}

// 🔥 카드 디자인 하나로 통합
@Composable
fun ContentActivityCard(item: ContentActivityItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .padding(16.dp)
    ) {
        // [상단] 프로필, 닉네임, 찬/반 뱃지, 시간
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            ProfileImage(
                model = R.drawable.ic_profile_mengzi,
                modifier = Modifier.size(36.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.nickname,
                        style = SwypTheme.typography.labelMedium,
                        color = Gray900
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    val isAgree = item.stance == "찬성"
                    val badgeBgColor = if (isAgree) Beige400 else SwypTheme.colors.primary
                    val badgeTextColor = if (isAgree) SwypTheme.colors.primary else SwypTheme.colors.surface

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(2.dp))
                            .background(badgeBgColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = item.stance,
                            style = SwypTheme.typography.b5Medium,
                            color = badgeTextColor
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.timeAgo,
                    style = SwypTheme.typography.b4Regular,
                    color = Gray500
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // [중단] 본문
        Text(
            text = item.content,
            style = SwypTheme.typography.b3Regular,
            color = Gray600,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))

        // [하단] 좋아요 수 (우측 정렬)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_heart_plus),
                contentDescription = "좋아요",
                modifier = Modifier.size(16.dp),
                tint = Gray500
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = item.likeCount,
                style = SwypTheme.typography.labelMedium,
                color = Gray500
            )
        }
    }
}