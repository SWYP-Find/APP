package com.picke.presentation.ui.my.content

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.domain.feature.mypage.model.MyContentActivityItem
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomTabBar
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.toRelativeTimeText
import kotlinx.coroutines.launch

@Composable
fun ContentActivityScreen(
    onBackClick: () -> Unit,
    onNavigateToComment: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ContentActivityViewModel = hiltViewModel()
) {
    val tabs = listOf("내 댓글·관점", "좋아요")
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBrand,
        topBar = {
            CustomTopAppBar(
                title = stringResource(R.string.my_menu_content),
                centerTitle = true,
                showLogo = false,
                showBackButton = true,
                onBackClick = { onBackClick() },
                backgroundColor = PickeTheme.colors.backgroundBrand
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .fillMaxSize()
        ) {
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

            if (uiState.isLoading) {
                ContentActivitySkeleton(modifier = Modifier.fillMaxSize())
            } else {
                // 페이지 내용
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    when (page) {
                        0 -> ContentActivityList(
                            items = uiState.commentList,
                            activityType = "COMMENT",
                            emptyMessage = "아직 작성한 댓글이나 관점이 없습니다",
                            onItemClick = onNavigateToComment,
                            onLoadMore = { viewModel.loadMore("COMMENT") }
                        )

                        1 -> ContentActivityList(
                            items = uiState.likeList,
                            activityType = "LIKE",
                            emptyMessage = "아직 좋아요를 누른 콘텐츠가 없습니다",
                            onItemClick = onNavigateToComment,
                            onLoadMore = { viewModel.loadMore("LIKE") }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContentActivityList(
    items: List<MyContentActivityItem>,
    activityType: String,
    emptyMessage: String,
    onItemClick: (String) -> Unit,
    onLoadMore: () -> Unit
) {
    if (items.isEmpty()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.logo_picke),
                contentDescription = "빈 화면 로고",
                modifier = Modifier.size(width = 160.dp, height = 120.dp),
                tint = PickeTheme.colors.borderDefault
            )
            Text(
                text = emptyMessage,
                style = PickeTheme.typography.b3Regular,
                color = PickeTheme.colors.beige800
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(items) { index, item ->
                if (index >= items.size - 2) {
                    onLoadMore()
                }

                ContentActivityCard(
                    item = item,
                    onClick = { onItemClick(item.perspectiveId) }
                )
            }
        }
    }
}

@Composable
fun ContentActivityCard(
    item: MyContentActivityItem,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surface)
            .border(1.dp, PickeTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        // [상단] 프로필, 닉네임, 찬/반 뱃지, 시간
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 프로필 이미지
            ProfileImage(
                model = item.author.characterImageUrl,
                modifier = Modifier.size(36.dp),
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.author.nickname,
                        style = PickeTheme.typography.labelMedium,
                        color = PickeTheme.colors.textTertiary
                    )
                    Spacer(modifier = Modifier.width(6.dp))

                    if (item.optionTitle != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(2.dp))
                                .background(PickeTheme.colors.surfaceTertiary)
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = item.optionTitle ?: "",
                                style = PickeTheme.typography.b5Medium,
                                color = PickeTheme.colors.primary
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = item.createdAt.toRelativeTimeText(),
                    style = PickeTheme.typography.b4Regular,
                    color = PickeTheme.colors.textMuted
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // [중단] 본문
        Text(
            text = item.content,
            style = PickeTheme.typography.b3Regular,
            color = PickeTheme.colors.neutral600,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

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
                tint = PickeTheme.colors.textMuted
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = item.likeCount.toString(),
                style = PickeTheme.typography.labelMedium,
                color = PickeTheme.colors.textMuted
            )
        }
    }
}