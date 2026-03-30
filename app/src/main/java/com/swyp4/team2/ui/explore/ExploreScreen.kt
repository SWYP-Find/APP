package com.swyp4.team2.ui.explore

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.SortFilterChip
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray500
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary50
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
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
                actions = {
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
                }
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
                ) { _ ->
                    ExploreList(
                        pagingItems = pagingItems,
                        isLoading = isLoading,
                        selectedSort = selectedSort,
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
    onSortChanged: (String) -> Unit,
    onNavigateToVote: (String) -> Unit
) {
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
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(count = pagingItems.itemCount) { index ->
                    pagingItems[index]?.let { item ->
                        ExploreCard(
                            item = item,
                            onClick = { id -> onNavigateToVote(id) }
                        )
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
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .clickable { onClick(item.battleId) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // 썸네일 이미지
        AsyncImage(
            model = item.thumbnailUrl,
            contentDescription = "Content Thumbnail",
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
        )

        // 텍스트 정보들
        Column(modifier = Modifier.weight(1f)) {
            // 1. 타입(뱃지) & 제목
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Beige600)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = item.type,
                        style = SwypTheme.typography.h5SemiBold,
                        color = SwypTheme.colors.primary
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = item.title,
                    style = SwypTheme.typography.h5SemiBold,
                    color = Gray500,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // 2. 태그 리스트
            if (item.tags.isNotEmpty()) {
                Text(
                    text = item.tags.joinToString(" ") { "#$it" },
                    style = SwypTheme.typography.label,
                    color = SwypTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
            }

            // 3. 설명 내용
            Text(
                text = item.summary,
                style = SwypTheme.typography.b4Regular,
                color = Gray400,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 4. 오디오 시간 & 조회수
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Gray300
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.audioDurationText, style = SwypTheme.typography.label, color = Gray400)

                Spacer(modifier = Modifier.width(12.dp))

                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Gray300
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.viewCountText, style = SwypTheme.typography.label, color = Gray400)
            }
        }
    }
}