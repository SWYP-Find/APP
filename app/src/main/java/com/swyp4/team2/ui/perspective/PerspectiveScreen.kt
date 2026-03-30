package com.swyp4.team2.ui.perspective

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.PerspectiveStance
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.SortFilterChip
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Beige800
import com.swyp4.team2.ui.theme.Gray200
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary50
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.Primary600
import com.swyp4.team2.ui.theme.Primary900
import com.swyp4.team2.ui.theme.SwypTheme
import kotlinx.coroutines.launch

@Composable
fun PerspectiveScreen(
    onBackClick: ()->Unit,
    onNextClick: ()->Unit,
    onMoreClick: (String)->Unit,
    modifier: Modifier = Modifier,
    viewModel: PerspectiveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tabList = listOf("전체", "찬성", "반대")
    var inputText by remember { mutableStateOf("") }
    val pagerState = rememberPagerState(pageCount = { tabList.size })
    val coroutineScope = rememberCoroutineScope()

    val pullToRefreshState = rememberPullToRefreshState()

    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRefreshing = false
        }
    }

    Scaffold(
        containerColor = Beige200,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "배틀 제목",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = false,
                    onBackClick = onBackClick,
                    backgroundColor = Beige200,
                    actions = {
                        IconButton(onClick = { onNextClick() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = "null",
                                tint = Gray900,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                PerspectiveInputField(
                    inputText = inputText,
                    onTextChanged = { inputText = it },
                    onSubmit = {
                        viewModel.submitPerspective(inputText) {
                            inputText = ""
                        }
                    }
                )
            }
        }
    ){ innerPadding ->
        if (uiState.isLoading && uiState.perspectives.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary900)
            }
        } else {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    PerspectiveHeader(
                        proPercentage = uiState.proRatio,
                        conPercentage = uiState.conRatio,
                        opinionChanged = uiState.opinionChanged
                    )

                    CustomTabBar(
                        tabs = tabList,
                        selectedTab = tabList[pagerState.currentPage],
                        isScrollable = false,
                        onTabSelected = { selected ->
                            val targetIndex = tabList.indexOf(selected)
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(targetIndex)
                            }
                        }
                    )

                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.Top
                    ) { pageIndex ->
                        val filteredList = when (pageIndex) {
                            1 -> uiState.perspectives.filter { it.stance == "찬성" } // 찬성 탭
                            2 -> uiState.perspectives.filter { it.stance == "반대" } // 반대 탭
                            else -> uiState.perspectives // 전체 탭
                        }
                        Column(modifier = Modifier.fillMaxSize()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                SortFilterChip(
                                    text = "인기순",
                                    isSelected = uiState.sort == "popular",
                                    onClick = { viewModel.updateSort("popular") }
                                )

                                SortFilterChip(
                                    text = "최신순",
                                    isSelected = uiState.sort == "latest",
                                    onClick = { viewModel.updateSort("latest") }
                                )
                            }

                            PullToRefreshBox(
                                isRefreshing = isRefreshing,
                                onRefresh = {
                                    isRefreshing = true
                                    viewModel.refreshAllData()
                                },
                                modifier = Modifier.fillMaxSize(),
                                indicator = {
                                    PullToRefreshDefaults.Indicator(
                                        state = pullToRefreshState,
                                        isRefreshing = isRefreshing,
                                        containerColor = Color.White,
                                        color = SwypTheme.colors.primary
                                    )
                                }
                            ) {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                    verticalArrangement = Arrangement.spacedBy(12.dp)
                                ) {

                                    uiState.myPerspective?.let { myView ->
                                        if (myView.status != "PUBLISHED") {
                                            item {
                                                val myLabel =
                                                    if (myView.optionLabel == "A" || myView.optionLabel == "AGREE") "찬성" else "반대"

                                                PerspectiveItemCard(
                                                    item = PerspectiveUiModel(
                                                        commentId = "-1",
                                                        profileImageUrl = "",
                                                        nickname = "나",
                                                        stance = myView.optionLabel,
                                                        content = myView.content,
                                                        timeAgo = "방금 전",
                                                        replyCount = 0,
                                                        likeCount = 0,
                                                        isLiked = false,
                                                        isMine = true
                                                    ),
                                                    status = myView.status,
                                                    clickable = false
                                                )
                                            }
                                        }
                                    }

                                    if (filteredList.isEmpty() && !uiState.isLoading) {
                                        item {
                                            // 탭의 pageIndex에 따라 빈 화면 문구 다르게 설정!
                                            val emptyMsg = when (pageIndex) {
                                                1 -> "아직 작성된 찬성 관점이 없습니다"
                                                2 -> "아직 작성된 반대 관점이 없습니다"
                                                else -> "아직 작성된 관점이 없습니다"
                                            }

                                            // LazyColumn 안에서 중앙에 오도록 fillParentMaxSize() 사용
                                            PerspectiveEmptyState(
                                                message = emptyMsg,
                                                modifier = Modifier.fillParentMaxSize()
                                            )
                                        }
                                    } else {
                                        itemsIndexed(filteredList) { index, item ->
                                            PerspectiveItemCard(
                                                item = item,
                                                onMoreClick = { onMoreClick(item.commentId) },
                                                onEditClick = { content ->
                                                    inputText = content
                                                    viewModel.setEditMode(
                                                        item.commentId.toLongOrNull() ?: 0L
                                                    )
                                                },
                                                onDeleteClick = {
                                                    viewModel.deletePerspective(
                                                        item.commentId.toLongOrNull() ?: 0L
                                                    )
                                                },
                                                onLikeClick = {
                                                    viewModel.toggleLike(
                                                        perspectiveId = item.commentId.toLongOrNull()
                                                            ?: 0L,
                                                        isCurrentlyLiked = item.isLiked
                                                    )
                                                }
                                            )

                                            if (index == filteredList.lastIndex && uiState.hasNext && !uiState.isLoading) {
                                                LaunchedEffect(index) {
                                                    viewModel.loadPerspectives()
                                                }
                                            }
                                        }

                                        if (uiState.isLoading) {
                                            item {
                                                Box(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(16.dp),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    CircularProgressIndicator(color = Primary900)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun PerspectiveItemCard(
    item: PerspectiveUiModel,
    status: String? = null,
    isDetail: Boolean = false,
    modifier: Modifier = Modifier,
    onMoreClick: () -> Unit = {},
    clickable: Boolean = true,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLikeClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val cardBgColor = if (status == "REJECTED") Color(0xFFFFF6F6) else Color.White

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (isDetail) null else BorderStroke(width = 1.dp, color = Beige600)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {

            // 1. 프로필 영역 (기존 코드와 완벽히 동일)
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = item.profileImageUrl,
                    contentDescription = "캐릭터 프로필",
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.nickname,
                            style = SwypTheme.typography.labelMedium,
                            color = Gray700
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = SwypTheme.colors.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text = item.stance,
                                style = SwypTheme.typography.b5Medium,
                                color = SwypTheme.colors.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        if (status == "PENDING" || status == "REJECTED") {
                            Spacer(modifier = Modifier.width(6.dp))
                            val isRejected = status == "REJECTED"
                            Surface(
                                color = if (isRejected) Color(0xFFFFF0F0) else Gray200,
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Text(
                                    text = if (isRejected) "거절됨" else "검수중",
                                    style = SwypTheme.typography.b5Medium,
                                    color = if (isRejected) Color(0xFFFF5454) else Gray600,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                    Text(
                        text = item.timeAgo,
                        style = SwypTheme.typography.labelXSmall,
                        color = SwypTheme.colors.outline
                    )
                }

                // 케밥 메뉴
                if (status != "PENDING" && status != "REJECTED") {
                    Box {
                        IconButton(
                            onClick = {
                                if (clickable) isMenuExpanded = true else isMenuExpanded = false
                            },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(painterResource(id = R.drawable.ic_more), "더보기", tint = Gray300)
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                            modifier = Modifier.background(Primary600)
                                .clip(RoundedCornerShape(8.dp))
                        ) {
                            PerspectiveMenuItem(iconRes = R.drawable.ic_trash, text = "삭제") {
                                isMenuExpanded = false
                                onDeleteClick()
                            }
                            PerspectiveMenuItem(iconRes = R.drawable.ic_edit, text = "수정") {
                                isMenuExpanded = false
                                onEditClick(item.content)
                            }
                            PerspectiveMenuItem(iconRes = R.drawable.ic_bell, text = "신고") {
                                isMenuExpanded = false
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. 본문 영역: 상세(isDetail)면 무제한, 아니면 3줄 제한!
            Text(
                text = item.content,
                style = SwypTheme.typography.b4Regular,
                color = Gray600,
                maxLines = if (isDetail) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. 하단 영역
            if (status != "PENDING" && status != "REJECTED") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!isDetail) {
                        Text(
                            text = "더보기",
                            style = SwypTheme.typography.b5Medium,
                            color = Gray300,
                            modifier = if (clickable) Modifier.clickable { onMoreClick() } else Modifier
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))

                    if (!isDetail) {
                        Icon(
                            painterResource(id = R.drawable.ic_message),
                            "댓글",
                            Modifier.size(12.dp),
                            tint = Gray300
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "${item.replyCount}",
                            style = SwypTheme.typography.b5Medium,
                            color = Gray300
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable { onLikeClick() }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_heart_plus),
                            contentDescription = "좋아요",
                            modifier = Modifier.size(12.dp),
                            tint = if (item.isLiked) SwypTheme.colors.primary else Gray300 // 🌟 색상 연동
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${item.likeCount}",
                            style = SwypTheme.typography.b5Medium,
                            color = if (item.isLiked) SwypTheme.colors.primary else Gray300 // 🌟 숫자도 색상 연동
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun PerspectiveMenuItem(
    iconRes: Int,
    text: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            style = SwypTheme.typography.labelMedium,
            color = Color.White
        )
    }
}


@Composable
fun PerspectiveInputField(
    inputText: String,
    onTextChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 키보드가 올라올 때 입력창이 위로 올라가도록 imePadding() 적용
    Surface(
        color = SwypTheme.colors.surface,
        shadowElevation = 16.dp, // 상단에 그림자 효과를 줘서 리스트와 구분
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Bottom // 마법봉 아이콘을 아래쪽으로 정렬
        ) {
            // 텍스트 입력 영역 (베이지색 박스)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFFFBF9F6), RoundedCornerShape(8.dp)) // 시안의 연한 베이지 배경
                    .padding(12.dp)
            ) {
                // Hint (입력된 글자가 없을 때만 보임)
                if (inputText.isEmpty()) {
                    Text(
                        text = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요...",
                        style = SwypTheme.typography.b3Regular,
                        color = SwypTheme.colors.outline,
                        lineHeight = 20.sp
                    )
                }

                BasicTextField(
                    value = inputText,
                    onValueChange = { if (it.length <= 200) onTextChanged(it) }, // 200자 제한
                    textStyle = SwypTheme.typography.b3Regular.copy(color = Gray900, lineHeight = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp) // 글자수 카운터 공간 확보
                )

                // 글자 수 카운터 (우측 하단)
                Text(
                    text = "${inputText.length}/200",
                    style = SwypTheme.typography.labelXSmall,
                    color = SwypTheme.colors.outline,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 보내기(마법봉) 버튼
            IconButton(
                onClick = onSubmit,
                modifier = Modifier.size(40.dp)
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_send),
                    contentDescription = "등록",
                    tint = SwypTheme.colors.primary
                )
            }
        }
    }
}

@Composable
fun PerspectiveHeader(
    proPercentage: Float = 59.5f,
    conPercentage: Float = 40.5f,
    opinionChanged: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // 1. 생각이 바뀌었어요 버튼
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                color = Primary50,
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_think),
                        contentDescription = "생각 변경",
                        tint = Primary500,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (opinionChanged) "생각이 바뀌었어요" else "생각이 동일해요",
                        style = SwypTheme.typography.caption2SemiBold,
                        color = Primary500
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2. 찬/반 비율 바
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "찬 ${proPercentage}%",
                style = SwypTheme.typography.label,
                color = Gray600
            )

            Spacer(modifier = Modifier.width(12.dp))

            // 비율에 따라 채워지는 프로그레스 바
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
            ) {
                // 찬성 비율 (진한 붉은색)
                Box(
                    modifier = Modifier
                        .weight(if (proPercentage > 0) proPercentage else 0.1f)
                        .fillMaxHeight()
                        .background(Color(0xFFA64D47))
                )
                // 반대 비율 (연한 회색)
                Box(
                    modifier = Modifier
                        .weight(if (conPercentage > 0) conPercentage else 0.1f)
                        .fillMaxHeight()
                        .background(Color(0xFFEBEBEB))
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "반 ${conPercentage}%",
                style = SwypTheme.typography.label,
                color = Gray600
            )
        }
    }
}

@Composable
fun PerspectiveEmptyState(
    message: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 80.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "빈 화면 로고",
            modifier = Modifier.size(width=160.dp, height=120.dp),
            tint = Beige600
        )
        Text(
            text = message,
            style = SwypTheme.typography.b3Regular,
            color = Beige800
        )
    }
}