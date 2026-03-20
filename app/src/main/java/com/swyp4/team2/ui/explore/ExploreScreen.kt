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
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.AppRoute
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.explore.model.ExploreItem
import com.swyp4.team2.ui.explore.model.dummyExploreList
import com.swyp4.team2.ui.theme.Beige100
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray400
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary50
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun ExploreScreen(
    viewModel: ExploreViewModel = hiltViewModel(),
    onNavigateToAlarm: ()->Unit,
    onNavigateToVote: (Int) -> Unit,
) {
    val exploreCategories = listOf("전체", "철학", "문학", "예술", "과학", "사회", "역사")
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val categoryList by viewModel.categoryList.collectAsState()
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
        containerColor = SwypTheme.colors.surface,
        topBar = {
            CustomTopAppBar(
                showLogo = true,
                centerTitle = false,
                backgroundColor = SwypTheme.colors.surface,
                actions = {
                    IconButton(
                        onClick = {
                            onNavigateToAlarm()
                            // 클릭했으니까 빨간 점 없애기 (선택사항)
                            hasUnreadNotification = false
                        }
                    ) {
                        BadgedBox(
                            badge = {
                                if (hasUnreadNotification) {
                                    Badge(
                                        containerColor = SwypTheme.colors.primary
                                    )
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
    ){ innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CustomTabBar(
                tabs = exploreCategories,
                isScrollable = true,
                selectedTab = selectedCategory,
                onTabSelected = { clickedCategory ->
                    val targetPage = exploreCategories.indexOf(clickedCategory)
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(targetPage)
                    }
                }
            )

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                ExploreList(
                    category = exploreCategories[page],
                    onNavigateToVote = onNavigateToVote
                )
            }
        }
    }
}

@Composable
fun ExploreList(
    category: String,
    onNavigateToVote: (Int) -> Unit
) {
    var selectedSort by remember { mutableStateOf("인기순") }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SortButton(
                    text = stringResource(R.string.explore_hot_rank),
                    isSelected = selectedSort == "인기순",
                    onClick = { selectedSort = "인기순" }
                )
                SortButton(
                    text =  stringResource(R.string.explore_recent_rank),
                    isSelected = selectedSort == "최신순",
                    onClick = { selectedSort = "최신순" }
                )
            }
        }

        // 🌟 2. 그 아래에 카드 리스트를 그립니다.
        items(dummyExploreList) { item ->
            ExploreCard(
                item = item,
                onClick = { id -> onNavigateToVote(id) }
            )
        }
    }
}

@Composable
fun SortButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    // 선택 여부에 따라 색상 반전 처리
    val backgroundColor = if (isSelected) SwypTheme.colors.primary else Primary50
    val contentColor = if (isSelected) Primary50 else SwypTheme.colors.primary
    val borderColor = SwypTheme.colors.primary

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(4.dp))
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = SwypTheme.typography.b4Medium, // 폰트 사이즈가 작아 보여서 b4Medium으로 설정했습니다. 필요시 조정하세요!
            color = contentColor
        )
    }
}

@Composable
fun ExploreCard(
    item: ExploreItem,
    onClick: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(SwypTheme.colors.surface)
            .border(1.dp, Beige600, RoundedCornerShape(2.dp))
            .clickable{ onClick(item.id) }
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 썸네일 이미지
        AsyncImage(
            model = item.imageUrl,
            contentDescription = "Content Thumbnail",
            modifier = Modifier
                .size(80.dp)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
        )

        // 텍스트 정보들
        Column(
            modifier = Modifier.weight(1f)
        ) {
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
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = item.title,
                    style = SwypTheme.typography.h5SemiBold,
                    color = Gray900,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 2. 설명 내용
            Text(
                text = item.description,
                style = SwypTheme.typography.b4Regular,
                color = Gray600,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. 시간 & 조회수 (오른쪽 정렬)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // tts 시간
                Icon(
                    painter = painterResource(id = R.drawable.ic_clock),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Gray400
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.timeAgo, style = SwypTheme.typography.label, color = Gray400)

                Spacer(modifier = Modifier.width(12.dp))

                // 조회수
                Icon(
                    painter = painterResource(id = R.drawable.ic_eye),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Gray400
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = item.viewCount, style = SwypTheme.typography.label, color = Gray400)
            }
        }
    }
}