package com.picke.app.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.picke.app.R
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.CustomTabBar
import com.picke.app.ui.component.SortFilterChip
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Beige800
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray400
import com.picke.app.ui.theme.Gray500
import com.picke.app.ui.theme.Primary900
import com.picke.app.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    scrollToTopTrigger: Int = 0,
    onNavigateToAlarm: ()->Unit,
    onNavigateToVote: (String) -> Unit,
) {
    val exploreCategories = listOf("전체", "철학", "문학", "예술", "과학", "사회", "역사")
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val pagingItems = viewModel.explorePagingData.collectAsLazyPagingItems()

    val pagerState = rememberPagerState(pageCount = { exploreCategories.size })
    val coroutineScope = rememberCoroutineScope()
    var hasUnreadNotification by remember { mutableStateOf(false) }

    LaunchedEffect(pagerState.currentPage) {
        val currentCategory = exploreCategories[pagerState.currentPage]
        if (selectedCategory != currentCategory) {
            viewModel.updateCategory(currentCategory)
        }
    }

    Scaffold(
        containerColor = Beige200,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = Beige200,
                /*actions = {
                    IconButton(
                        onClick = {
                            onNavigateToAlarm()
                            hasUnreadNotification = false
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (hasUnreadNotification) {
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
                }*/
            )
        }
    ) { innerPadding ->
        val isLoading = pagingItems.loadState.refresh is LoadState.Loading
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomTabBar(
                    tabs = exploreCategories,
                    isScrollable = true,
                    selectedTab = selectedCategory,
                    onTabSelected = { clickedCategory ->
                        val targetPage = exploreCategories.indexOf(clickedCategory)
                        coroutineScope.launch { pagerState.animateScrollToPage(targetPage) }
                    }
                )

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                        .background(Color.White)
                ) { _ ->
                    ExploreList(
                        pagingItems = pagingItems,
                        isLoading = isLoading,
                        selectedSort = selectedSort,
                        scrollToTopTrigger = scrollToTopTrigger,
                        onSortChanged = { newSort -> viewModel.updateSort(newSort) },
                        onNavigateToVote = onNavigateToVote
                    )
                }
            }

    }
}
@Composable
fun ExploreList(
    pagingItems: LazyPagingItems<ExploreUiModel>,
    isLoading: Boolean,
    selectedSort: String,
    scrollToTopTrigger: Int = 0,
    onSortChanged: (String) -> Unit,
    onNavigateToVote: (String) -> Unit
) {
    val listState = rememberLazyListState()

    LaunchedEffect(scrollToTopTrigger) {
        if (scrollToTopTrigger > 0) {
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            SortFilterChip(
                text = stringResource(R.string.explore_hot_rank),
                isSelected = selectedSort == "POPULAR",
                onClick = { onSortChanged("POPULAR") }
            )
            SortFilterChip(
                text = stringResource(R.string.explore_recent_rank),
                isSelected = selectedSort == "LATEST",
                onClick = { onSortChanged("LATEST") }
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        }
        else if (pagingItems.itemCount == 0) {
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "아직 준비된 배틀이 없어요!",
                    style = SwypTheme.typography.b3Regular,
                    color = Beige800
                )
            }
        }
        else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                //verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(count = pagingItems.itemCount) { index ->
                    pagingItems[index]?.let { item ->
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = Beige600,
                        )
                        ExploreCard(
                            item = item,
                            onClick = { id -> onNavigateToVote(id) }
                        )
                        if (index == pagingItems.itemCount - 1) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = Beige600,
                            )
                        }
                    }
                }

                if (pagingItems.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary900, modifier = Modifier.size(24.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExploreCard(
    item: ExploreUiModel,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .clickable { onClick(item.battleId) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 썸네일 이미지
        SubcomposeAsyncImage(
            model = item.thumbnailUrl,
            contentDescription = "Content Thumbnail",
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Beige200),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = SwypTheme.colors.primary,
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                }
            }
        )

        // 텍스트 정보들
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // 1. 타입(뱃지) & 제목
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = item.title,
                    style = SwypTheme.typography.h5SemiBold.copy(
                        lineBreak = LineBreak(
                            strategy = LineBreak.Strategy.HighQuality,
                            strictness = LineBreak.Strictness.Loose,
                            wordBreak = LineBreak.WordBreak.Default
                        )
                    ),
                    color = Gray500,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 2. 설명 내용
            Text(
                text = item.summary,
                style = SwypTheme.typography.b4Regular,
                color = Gray400,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            // 3. 태그 내용 & 오디오 시간/조회수
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // [왼쪽 그룹] 해시태그
                Text(
                    text = item.tags.joinToString(" ") { "#$it" },
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // 태그와 아이콘 사이의 최소한의 간격
                Spacer(modifier = Modifier.width(8.dp))

                // [오른쪽 그룹] 오디오 시간 & 조회수
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Gray300
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.audioDurationText,
                        style = SwypTheme.typography.label,
                        color = Gray400
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Gray300
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.viewCountText,
                        style = SwypTheme.typography.label,
                        color = Gray400
                    )
                }
            }
        }
    }
}