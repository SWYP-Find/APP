package com.picke.presentation.ui.explore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.picke.presentation.BuildConfig
import com.picke.presentation.R
import com.picke.presentation.ui.component.AdFitBannerAd
import com.picke.presentation.ui.component.CustomTabBar
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.SortFilterChip
import com.picke.presentation.ui.explore.component.ExploreCard
import com.picke.presentation.ui.explore.component.ExploreSkeleton
import com.picke.presentation.ui.explore.model.ExploreUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    scrollToTopTrigger: Int = 0,
    onNavigateToAlarm: () -> Unit,
    onNavigateToVote: (String) -> Unit,
) {
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedSort by viewModel.selectedSort.collectAsState()
    val pagingItems = viewModel.explorePagingData.collectAsLazyPagingItems()

    ExploreScreen(
        selectedCategory = selectedCategory,
        selectedSort = selectedSort,
        pagingItems = pagingItems,
        scrollToTopTrigger = scrollToTopTrigger,
        onCategoryChange = { viewModel.updateCategory(it) },
        onSortChange = { viewModel.updateSort(it) },
        onNavigateToAlarm = onNavigateToAlarm,
        onNavigateToVote = onNavigateToVote
    )
}

@Composable
fun ExploreScreen(
    selectedCategory: String,
    selectedSort: String,
    pagingItems: LazyPagingItems<ExploreUiModel>,
    scrollToTopTrigger: Int,
    onCategoryChange: (String) -> Unit,
    onSortChange: (String) -> Unit,
    onNavigateToAlarm: () -> Unit,
    onNavigateToVote: (String) -> Unit
) {
    val exploreCategories = listOf("전체", "철학", "문학", "예술", "과학", "사회", "역사")
    val pagerState = rememberPagerState(pageCount = { exploreCategories.size })
    val coroutineScope = rememberCoroutineScope()
    var hasUnreadNotification by remember { mutableStateOf(false) }

    val isLoading = pagingItems.loadState.refresh is LoadState.Loading

    LaunchedEffect(pagerState.currentPage) {
        val currentCategory = exploreCategories[pagerState.currentPage]
        if (selectedCategory != currentCategory) {
            onCategoryChange(currentCategory)
        }
    }

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBrand,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = PickeTheme.colors.backgroundBrand,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTabBar(
                tabs = exploreCategories,
                isScrollable = false,
                selectedTab = selectedCategory,
                onTabSelected = { clickedCategory ->
                    val targetPage = exploreCategories.indexOf(clickedCategory)
                    coroutineScope.launch { pagerState.animateScrollToPage(targetPage) }
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) { _ ->
                ExploreList(
                    pagingItems = pagingItems,
                    isLoading = isLoading,
                    selectedSort = selectedSort,
                    scrollToTopTrigger = scrollToTopTrigger,
                    onSortChanged = onSortChange,
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
        } else if (pagingItems.itemCount == 0) {
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
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "아직 준비된 배틀이 없어요!",
                    style = PickeTheme.typography.b3Regular,
                    color = PickeTheme.colors.beige800
                )
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
            ) {
                val topCount = minOf(3, pagingItems.itemCount)

                items(count = topCount) { index ->
                    pagingItems[index]?.let { item ->
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = PickeTheme.colors.borderDefault,
                        )
                        ExploreCard(
                            item = item,
                            onClick = { id -> onNavigateToVote(id) }
                        )
                        if (index == pagingItems.itemCount - 1) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = PickeTheme.colors.borderDefault,
                            )
                        }
                    }
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        AdFitBannerAd(adUnitId = BuildConfig.ADFIT_BANNER_320X100)
                    }
                }

                items(count = pagingItems.itemCount - topCount) { offset ->
                    val index = topCount + offset
                    pagingItems[index]?.let { item ->
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = PickeTheme.colors.borderDefault,
                        )
                        ExploreCard(
                            item = item,
                            onClick = { id -> onNavigateToVote(id) }
                        )
                        if (index == pagingItems.itemCount - 1) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = PickeTheme.colors.borderDefault,
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
                            CircularProgressIndicator(
                                color = PickeTheme.colors.primaryDarkest,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreScreenPreview() {
    PickeTheme {
        val dummyPagingItems =
            flowOf(PagingData.from(DummyData.dummyExploreList)).collectAsLazyPagingItems()

        ExploreScreen(
            selectedCategory = "전체",
            selectedSort = "POPULAR",
            pagingItems = dummyPagingItems,
            scrollToTopTrigger = 0,
            onCategoryChange = {},
            onSortChange = {},
            onNavigateToAlarm = {},
            onNavigateToVote = {}
        )
    }
}