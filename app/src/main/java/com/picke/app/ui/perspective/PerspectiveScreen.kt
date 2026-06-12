package com.picke.app.ui.perspective

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.foundation.lazy.LazyListState
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import com.picke.app.R
import com.picke.app.domain.model.VoteStatsOptionBoard
import com.picke.app.ui.component.CustomConfirmDialog
import com.picke.app.ui.component.CustomTabBar
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.ProfileImage
import com.picke.app.ui.component.SortFilterChip
import com.picke.app.ui.theme.SwypTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerspectiveScreen(
    onBackClick: ()->Unit,
    onNextClick: (String)->Unit,
    onMoreClick: (String)->Unit,
    modifier: Modifier = Modifier,
    viewModel: PerspectiveViewModel = hiltViewModel()
) {
    BackHandler {
        onBackClick()
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = androidx.compose.ui.platform.LocalContext.current
    val focusManager = androidx.compose.ui.platform.LocalFocusManager.current

    val voteOptions = uiState.voteOptions
    val tabList = remember(voteOptions) {
        listOf("전체") + voteOptions.map { it.title }
    }
    var inputText by remember { mutableStateOf("") }
    val pagerState = rememberPagerState(pageCount = { tabList.size })
    val coroutineScope = rememberCoroutineScope()

    val pullToRefreshState = rememberPullToRefreshState()

    var isRefreshing by remember { mutableStateOf(false) }
    var isSorting by remember { mutableStateOf(false) }

    var perspectiveToDelete by remember { mutableStateOf<Long?>(null) }
    var perspectiveToReport by remember { mutableStateOf<Long?>(null) }
    var scrollToTopTrigger by remember { mutableStateOf(0) }
    val isShowingMyPendingOrRejected = uiState.myPerspective?.let { it.status != "PUBLISHED" } ?: false

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is PerspectiveUiEvent.ShowToast -> {
                    android.widget.Toast.makeText(
                        context,
                        event.message,
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            if (isRefreshing || isSorting) {
                scrollToTopTrigger++
            }
            isRefreshing = false
            isSorting = false
        }
    }

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "관점 남기기",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = false,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.backgroundBrand,
                    actions = {
                        IconButton(onClick = { onNextClick(uiState.battleId) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = "null",
                                tint = SwypTheme.colors.textPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                val myView = uiState.myPerspective
                val isEditing = uiState.editingPerspectiveId != null

                val isInputLocked = when {
                    isEditing -> false
                    myView != null -> true
                    else -> false
                }

                // 힌트 문구
                val inputHint = when {
                    isEditing -> "수정할 내용을 입력해주세요..."
                    myView?.status == "REJECTED" -> "거절된 관점이 있습니다. \n더보기 메뉴에서 수정을 눌러주세요."
                    myView != null -> "이미 내 관점을 등록했습니다."
                    else -> "본인의 관점을 적어주세요. \n관점은 하나만 작성할 수 있습니다."
                }

                PerspectiveInputField(
                    inputText = inputText,
                    onTextChanged = { inputText = it },
                    onSubmit = {
                        viewModel.submitPerspective(inputText) {
                            inputText = ""
                            focusManager.clearFocus()
                            scrollToTopTrigger++
                        }
                    },
                    isEnabled = !isInputLocked,
                    hintText = inputHint,
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. 투표 통계
            PerspectiveHeader(
                voteOptions = uiState.voteOptions,
                opinionChanged = uiState.opinionChanged
            )

            // 2. 전체/옵션별 탭
            CustomTabBar(
                tabs = tabList,
                selectedTab = tabList[pagerState.currentPage],
                isScrollable = tabList.size > 3,
                onTabSelected = { selected ->
                    val targetIndex = tabList.indexOf(selected)
                    coroutineScope.launch { pagerState.animateScrollToPage(targetIndex) }
                    val optionId = if (targetIndex == 0) null else voteOptions.getOrNull(targetIndex - 1)?.optionId
                    viewModel.selectOption(optionId)
                }
            )

            LaunchedEffect(pagerState.currentPage) {
                val optionId = if (pagerState.currentPage == 0) null else voteOptions.getOrNull(pagerState.currentPage - 1)?.optionId
                viewModel.selectOption(optionId)
            }

            // 3. 인기순/최신순 칩과 관점들
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { pageIndex ->
                val listState = remember(uiState.sort, uiState.selectedOptionId, pageIndex) { LazyListState() }
                LaunchedEffect(scrollToTopTrigger) {
                    if (scrollToTopTrigger > 0) {
                        kotlinx.coroutines.delay(50)
                        listState.scrollToItem(0)
                    }
                }
                // 서버 사이드 필터링: 각 탭 선택 시 optionId로 API 호출 → 별도 클라이언트 필터 불필요
                val filteredList = uiState.perspectives

                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SortFilterChip(
                            text = "최신순",
                            isSelected = uiState.sort == "latest",
                            onClick = {
                                if (uiState.sort != "latest") {
                                    isSorting = true
                                    viewModel.updateSort("latest")
                                }
                            }
                        )
                        SortFilterChip(
                            text = "인기순",
                            isSelected = uiState.sort == "popular",
                            onClick = {
                                if (uiState.sort != "popular") {
                                    isSorting = true
                                    viewModel.updateSort("popular")
                                }
                            }
                        )
                    }

                    // 로딩중 화면
                    if (uiState.isLoading && uiState.perspectives.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = SwypTheme.colors.primaryDarkest)
                        }
                    }
                    // 로딩 됐을때 화면
                    else {
                        // 새로고침 박스
                        PullToRefreshBox(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                isRefreshing = true
                                viewModel.refreshAllData()
                            },
                            modifier = Modifier.weight(1f),
                            indicator = {
                                PullToRefreshDefaults.Indicator(
                                    state = pullToRefreshState,
                                    isRefreshing = isRefreshing,
                                    containerColor = Color.White,
                                    color = SwypTheme.colors.primary,
                                    modifier = Modifier.align(Alignment.TopCenter)
                                )
                            }
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                uiState.myPerspective?.let { myView ->
                                    if (myView.status != "PUBLISHED") {
                                        item {
                                            PerspectiveItemCard(
                                                item = PerspectiveUiModel(
                                                    commentId = myView.perspectiveId.toString(),
                                                    profileImageUrl = myView.characterImageUrl,
                                                    nickname = "나",
                                                    optionTitle = myView.optionTitle,
                                                    optionId = myView.optionId,
                                                    content = myView.content,
                                                    timeAgo = "방금 전",
                                                    replyCount = 0,
                                                    likeCount = 0,
                                                    isLiked = false,
                                                    isMine = true
                                                ),
                                                firstOptionId = uiState.voteOptions.firstOrNull()?.optionId ?: 0L,
                                                status = myView.status,
                                                clickable = false,
                                                onEditClick = { content ->
                                                    inputText = content
                                                    viewModel.setEditMode(myView.perspectiveId ?: 0L)
                                                },
                                                onDeleteClick = {
                                                    perspectiveToDelete = myView.perspectiveId ?: 0L
                                                },
                                                onLikeClick = {
                                                    android.widget.Toast.makeText(
                                                        context,
                                                        "본인이 쓴 관점에는 좋아요를 누를 수 없습니다.",
                                                        android.widget.Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            )
                                        }
                                    }
                                }

                                // 관점 목록이 아무것도 없을때
                                if (filteredList.isEmpty() && !uiState.isLoading && !isShowingMyPendingOrRejected) {
                                    item {
                                        val emptyMsg = if (pageIndex == 0) {
                                            "아직 작성된 관점이 없습니다"
                                        } else {
                                            "아직 작성된 ${tabList.getOrElse(pageIndex) { "" }} 관점이 없습니다"
                                        }

                                        PerspectiveEmptyState(
                                            message = emptyMsg,
                                            modifier = Modifier.fillParentMaxSize()
                                        )
                                    }
                                }
                                // 관점 목록이 있을때
                                else {
                                    itemsIndexed(
                                        items = filteredList,
                                        key = { _, item -> item.commentId }
                                    ) { index, item ->
                                        PerspectiveItemCard(
                                            item = item,
                                            firstOptionId = uiState.voteOptions.firstOrNull()?.optionId ?: 0L,
                                            onMoreClick = { onMoreClick(item.commentId) },
                                            onEditClick = { content ->
                                                inputText = content
                                                viewModel.setEditMode(
                                                    item.commentId.toLongOrNull() ?: 0L
                                                )
                                            },
                                            onDeleteClick = {
                                                perspectiveToDelete = item.commentId.toLongOrNull() ?: 0L
                                            },
                                            onReportClick = {
                                                perspectiveToReport = item.commentId.toLongOrNull() ?: 0L
                                            },
                                            onLikeClick = {
                                                if (item.isMine) {
                                                    android.widget.Toast.makeText(
                                                        context,
                                                        "본인이 쓴 관점에는 좋아요를 누를 수 없습니다.",
                                                        android.widget.Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    // 남의 글이면 정상적으로 뷰모델의 좋아요 API 쏘기!
                                                    viewModel.toggleLike(
                                                        perspectiveId = item.commentId.toLongOrNull() ?: 0L,
                                                        isCurrentlyLiked = item.isLiked
                                                    )
                                                }
                                            },
                                        )
                                        val isAtEnd = index == filteredList.lastIndex
                                        val isNotLoading = !uiState.isLoading
                                        val hasMorePages = uiState.hasNext

                                        if (isAtEnd && isNotLoading && hasMorePages) {
                                            LaunchedEffect(item.commentId) {
                                                viewModel.loadPerspectives()
                                            }
                                        }
                                    }

                                    // 전체 로딩 화면
                                    if (uiState.isLoading && !isRefreshing) {
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                CircularProgressIndicator(color = SwypTheme.colors.primaryDarkest)
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

        // 삭제 다이얼로그 호출부
        if (perspectiveToDelete != null) {
            CustomConfirmDialog(
                message = "관점을 삭제하시겠습니까?",
                confirmText = "삭제하기",
                dismissText = "뒤로가기",
                onConfirm = {
                    // 왼쪽에 배치한 삭제 동작 실행
                    viewModel.deletePerspective(perspectiveToDelete!!)
                    perspectiveToDelete = null
                },
                onDismiss = {
                    // 오른쪽에 배치한 취소 동작 (닫기)
                    perspectiveToDelete = null
                }
            )
        }

        // 신고 다이얼로그 호출부
        if (perspectiveToReport != null) {
            CustomConfirmDialog(
                message = "관점을 신고하시겠습니까?",
                confirmText = "신고하기",
                dismissText = "뒤로가기",
                onConfirm = {
                    // 왼쪽에 배치한 신고 동작 실행
                    viewModel.reportPerspective(perspectiveToReport!!)
                    perspectiveToReport = null
                },
                onDismiss = {
                    // 오른쪽에 배치한 취소 동작 (닫기)
                    perspectiveToReport = null
                }
            )
        }
    }
}

@Composable
fun PerspectiveItemCard(
    item: PerspectiveUiModel,
    modifier: Modifier = Modifier,
    firstOptionId: Long = 0L,
    status: String? = null,
    isDetail: Boolean = false,
    onMoreClick: () -> Unit = {},
    clickable: Boolean = true,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onReportClick: () -> Unit = {},
) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val cardBgColor = when(status){
        "REJECTED" -> Color(0xFFFFF9F9)
        "PENDING" -> SwypTheme.colors.secondary50
        else -> Color.White
    }
    val borderBadgeColor = when (status) {
        "REJECTED" -> Color(0xFFA64D47)
        "PENDING" -> SwypTheme.colors.secondary
        else -> SwypTheme.colors.borderDefault
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = if (isDetail) null else BorderStroke(width = 1.dp, color = borderBadgeColor)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // 1. 프로필 영역
            Row(verticalAlignment = Alignment.CenterVertically) {
                ProfileImage(
                    model = item.profileImageUrl,
                    modifier = Modifier.size(32.dp).clip(CircleShape),
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // 닉네임
                        Text(
                            text = if (item.isMine) "나" else item.nickname,
                            style = SwypTheme.typography.labelMedium,
                            color = SwypTheme.colors.textSecondary
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        // 1. 검수중 또는 거절됨 상태일 때
                        if (status == "PENDING" || status == "REJECTED") {
                            Surface(
                                color = borderBadgeColor,
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Text(
                                    text = if (status == "PENDING") "검수중" else "거절됨",
                                    style = SwypTheme.typography.b5Medium,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        // 2. 일반 상태일 때 (입장별 뱃지 노출)
                        else {
                            val isPro = firstOptionId != 0L && item.optionId == firstOptionId
                            Surface(
                                color = if (isPro) SwypTheme.colors.borderDefault else SwypTheme.colors.primary,
                                shape = RoundedCornerShape(2.dp)
                            ) {
                                Text(
                                    text = item.optionTitle.ifEmpty { if (isPro) "A" else "B" },
                                    style = SwypTheme.typography.b5Medium,
                                    color = if (isPro) SwypTheme.colors.primary else Color.White,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
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

                if (status != "PENDING") {
                    Box {
                        IconButton(
                            onClick = { isMenuExpanded = true },
                            modifier = Modifier.size(16.dp)
                        ) {
                            Icon(painterResource(id = R.drawable.ic_more), "더보기", tint = SwypTheme.colors.textMuted)
                        }
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false },
                            modifier = Modifier.background(SwypTheme.colors.primaryPressed).clip(RoundedCornerShape(8.dp))
                        ) {
                            if (item.isMine) {
                                if (status != "REJECTED") {
                                    PerspectiveMenuItem(iconRes = R.drawable.ic_trash, text = "삭제") {
                                        isMenuExpanded = false
                                        onDeleteClick()
                                    }
                                }
                                PerspectiveMenuItem(iconRes = R.drawable.ic_edit, text = "수정") {
                                    isMenuExpanded = false
                                    onEditClick(item.content)
                                }
                            } else {
                                PerspectiveMenuItem(iconRes = R.drawable.ic_bell, text = "신고") {
                                    isMenuExpanded = false
                                    onReportClick()
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. 본문 영역
            Text(
                text = item.content,
                style = SwypTheme.typography.b4Regular,
                color = SwypTheme.colors.neutral600,
                maxLines = if (isDetail) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. 하단 영역 (더보기, 댓글 수, 좋아요)
            if (status != "PENDING" && status != "REJECTED") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (!isDetail) {
                        Text(
                            text = "더보기",
                            style = SwypTheme.typography.b5Medium,
                            color = SwypTheme.colors.textMuted,
                            modifier = Modifier.clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) {
                                if (clickable) onMoreClick()
                            }
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    if (!isDetail) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable(
                                interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                                indication = null
                            ) {
                                onMoreClick()
                            }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .clickable { onMoreClick() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_message),
                                    contentDescription = "댓글",
                                    modifier = Modifier.size(12.dp),
                                    tint = SwypTheme.colors.textMuted
                                )
                            }
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "${item.replyCount}",
                                style = SwypTheme.typography.b5Medium,
                                color = SwypTheme.colors.textMuted
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(
                            interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                            indication = null
                        ) {
                            onLikeClick()
                        }
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable { onLikeClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_heart_plus),
                                contentDescription = "좋아요",
                                modifier = Modifier.size(12.dp),
                                tint = if (item.isLiked) SwypTheme.colors.primary else SwypTheme.colors.textMuted
                            )
                        }
                        Text(
                            text = "${item.likeCount}",
                            style = SwypTheme.typography.b5Medium,
                            color = if (item.isLiked) SwypTheme.colors.primary else SwypTheme.colors.textMuted,
                            modifier = Modifier.padding(start = 2.dp)
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
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    hintText: String = "제도화가 무서운 건...",
) {
    Surface(
        color = SwypTheme.colors.surface,
        shadowElevation = 16.dp,
        modifier = modifier
            .fillMaxWidth()
            .imePadding()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            // 텍스트 입력 영역
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (isEnabled) SwypTheme.colors.backgroundBrand else SwypTheme.colors.beige100, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                // Hint
                if (inputText.isEmpty()) {
                    Text(
                        text = hintText,
                        style = SwypTheme.typography.b3Regular,
                        color = SwypTheme.colors.outline,
                        lineHeight = 20.sp
                    )
                }

                BasicTextField(
                    value = inputText,
                    onValueChange = { if (it.length <= 200) onTextChanged(it) },
                    enabled = isEnabled,
                    textStyle = SwypTheme.typography.b3Regular.copy(color = SwypTheme.colors.textPrimary, lineHeight = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                // 글자 수 카운터
                Text(
                    text = "${inputText.length}/200",
                    style = SwypTheme.typography.labelXSmall,
                    color = SwypTheme.colors.outline,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // 보내기 버튼
            IconButton(
                onClick = onSubmit,
                enabled = isEnabled,
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
    voteOptions: List<VoteStatsOptionBoard>,
    opinionChanged: Boolean,
    modifier: Modifier = Modifier
) {
    val leftOption = voteOptions.getOrNull(0)
    val rightOption = voteOptions.getOrNull(1)
    val proRatio = leftOption?.ratio ?: 50f
    val conRatio = rightOption?.ratio ?: 50f

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        // 왼쪽 옵션 (이미지 + 타이틀 + 비율)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(64.dp)
        ) {
            ProfileImage(
                model = leftOption?.imageUrl,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = leftOption?.title ?: "A",
                style = SwypTheme.typography.labelXSmall,
                color = SwypTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${proRatio.toInt()}%",
                style = SwypTheme.typography.label,
                color = SwypTheme.colors.neutral600
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 가운데: 생각이 바뀌었어요 버튼 + 프로그래스 바
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            // 1. 생각이 바뀌었어요 버튼
            Surface(
                color = SwypTheme.colors.primaryLight,
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_think),
                        contentDescription = "생각 변경",
                        tint = SwypTheme.colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (opinionChanged) "생각이 바뀌었어요" else "생각이 동일해요",
                        style = SwypTheme.typography.caption2SemiBold,
                        color = SwypTheme.colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. 비율에 따라 채워지는 프로그래스 바
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .weight(if (proRatio > 0) proRatio else 0.1f)
                        .fillMaxHeight()
                        .background(Color(0xFFA64D47))
                )
                Box(
                    modifier = Modifier
                        .weight(if (conRatio > 0) conRatio else 0.1f)
                        .fillMaxHeight()
                        .background(SwypTheme.colors.backgroundTertiary)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 오른쪽 옵션 (비율 + 타이틀 + 이미지)
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(64.dp)
        ) {
            ProfileImage(
                model = rightOption?.imageUrl,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = rightOption?.title ?: "B",
                style = SwypTheme.typography.labelXSmall,
                color = SwypTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${conRatio.toInt()}%",
                style = SwypTheme.typography.label,
                color = SwypTheme.colors.neutral600
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
            tint = SwypTheme.colors.borderDefault
        )
        Text(
            text = message,
            style = SwypTheme.typography.b3Regular,
            color = SwypTheme.colors.beige800
        )
    }
}