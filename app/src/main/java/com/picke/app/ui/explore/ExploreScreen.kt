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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.SubcomposeAsyncImage
import com.picke.app.BuildConfig
import com.picke.app.R
import com.picke.app.ui.component.AdFitBannerAd
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.CustomTabBar
import com.picke.app.ui.component.SortFilterChip
import com.picke.app.ui.component.shimmer
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
        containerColor = SwypTheme.colors.backgroundBrand,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = SwypTheme.colors.backgroundBrand,
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
                    // iOS 탐색탭처럼 카테고리 전체(전체·철학·문학·예술·과학·사회·역사)를
                    // 스크롤 없이 상단바에 등분 배치해 한 번에 보이도록 한다.
                    isScrollable = false,
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
            ExploreSkeleton(modifier = Modifier.fillMaxSize())
        }
        else if (pagingItems.itemCount == 0) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.logo_picke),
                    contentDescription = "빈 화면 로고",
                    modifier = Modifier.size(width = 160.dp, height = 120.dp),
                    tint = SwypTheme.colors.borderDefault
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "아직 준비된 배틀이 없어요!",
                    style = SwypTheme.typography.b3Regular,
                    color = SwypTheme.colors.beige800
                )
            }
        }
        else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                //verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                val adCount = pagingItems.itemCount / 3
                val totalCount = pagingItems.itemCount + adCount

                items(count = totalCount) { displayIndex ->
                    val cycleIndex = displayIndex % 4
                    val cycleNumber = displayIndex / 4

                    if (cycleIndex < 3) {
                        val battleIndex = cycleNumber * 3 + cycleIndex
                        if (battleIndex < pagingItems.itemCount) {
                            pagingItems[battleIndex]?.let { item ->
                                HorizontalDivider(
                                    thickness = 1.dp,
                                    color = SwypTheme.colors.borderDefault,
                                )
                                ExploreCard(
                                    item = item,
                                    onClick = { id -> onNavigateToVote(id) }
                                )
                                if (battleIndex == pagingItems.itemCount - 1) {
                                    HorizontalDivider(
                                        thickness = 1.dp,
                                        color = SwypTheme.colors.borderDefault,
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_320X100)
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
                            CircularProgressIndicator(color = SwypTheme.colors.primaryDarkest, modifier = Modifier.size(24.dp))
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
                        .shimmer()
                )
            }
        )

        // 텍스트 정보들
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            // 1. 카테고리 뱃지 & 제목 (iOS 탐색탭처럼 제목 앞에 카테고리 뱃지를 인라인 배치)
            Row(verticalAlignment = Alignment.Top) {
                item.tags.firstOrNull()?.let { category ->
                    Surface(
                        color = SwypTheme.colors.borderDefault,
                        shape = RoundedCornerShape(2.dp)
                    ) {
                        Text(
                            text = "#$category",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            // 피그마 스펙: 뱃지 텍스트 12sp (labelXSmall 기본 10sp에서 크기만 12로 조정)
                            style = SwypTheme.typography.labelXSmall.copy(fontSize = 12.sp),
                            color = SwypTheme.colors.primary,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = item.title,
                    style = SwypTheme.typography.b3SemiBold.copy(
                        lineBreak = LineBreak(
                            strategy = LineBreak.Strategy.HighQuality,
                            strictness = LineBreak.Strictness.Loose,
                            wordBreak = LineBreak.WordBreak.Default
                        )
                    ),
                    color = SwypTheme.colors.textTertiary,
                    // 제목이 1줄을 넘어가면 ...으로 말줄임 (iOS 탐색탭과 동일)
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // 2. 설명 내용
            Text(
                text = item.summary,
                style = SwypTheme.typography.b4Regular,
                color = SwypTheme.colors.neutral400,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            // 3. 오디오 시간/조회수 (카테고리는 제목 앞 뱃지로 이동, 하단은 시간/조회수만 우측 정렬)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // [오른쪽 그룹] 오디오 시간 & 조회수
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = SwypTheme.colors.textMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.audioDurationText,
                        style = SwypTheme.typography.label,
                        color = SwypTheme.colors.neutral400
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = SwypTheme.colors.textMuted
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.viewCountText,
                        style = SwypTheme.typography.label,
                        color = SwypTheme.colors.neutral400
                    )
                }
            }
        }
    }
}