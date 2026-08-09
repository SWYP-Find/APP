package com.picke.presentation.ui.perspective

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.domain.feature.vote.model.VoteStatsOptionBoard
import com.picke.presentation.R
import com.picke.presentation.ui.component.CustomConfirmDialog
import com.picke.presentation.ui.component.CustomTabBar
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.component.SortFilterChip
import com.picke.presentation.ui.perspective.component.PerspectiveEmptyState
import com.picke.presentation.ui.perspective.component.PerspectiveHeaderSkeleton
import com.picke.presentation.ui.perspective.component.PerspectiveInputField
import com.picke.presentation.ui.perspective.component.PerspectiveItemCard
import com.picke.presentation.ui.perspective.component.PerspectiveListSkeleton
import com.picke.presentation.ui.perspective.component.PerspectiveTabBarSkeleton
import com.picke.presentation.ui.perspective.model.PerspectiveUiEvent
import com.picke.presentation.ui.perspective.model.PerspectiveUiModel
import com.picke.presentation.ui.perspective.model.PerspectiveUiState
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerspectiveScreen(
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit,
    onMoreClick: (String, Long) -> Unit,
    scrollToCommentId: String? = null,
    modifier: Modifier = Modifier,
    viewModel: PerspectiveViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is PerspectiveUiEvent.ShowToast -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    PerspectiveScreenContent(
        uiState = uiState,
        onBackClick = onBackClick,
        onNextClick = onNextClick,
        onMoreClick = onMoreClick,
        scrollToCommentId = scrollToCommentId,
        modifier = modifier,
        onSubmitPerspective = { content, onSuccess ->
            viewModel.submitPerspective(content, onSuccess)
        },
        onSelectOption = { optionId ->
            viewModel.selectOption(optionId)
        },
        onUpdateSort = { sort ->
            viewModel.updateSort(sort)
        },
        onRefreshAllData = {
            viewModel.refreshAllData()
        },
        onSetEditMode = { id ->
            viewModel.setEditMode(id)
        },
        onDeletePerspective = { id ->
            viewModel.deletePerspective(id)
        },
        onReportPerspective = { id ->
            viewModel.reportPerspective(id)
        },
        onToggleLike = { id, isLiked ->
            viewModel.toggleLike(id, isLiked)
        },
        onLoadPerspectives = {
            viewModel.loadPerspectives()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PerspectiveScreenContent(
    uiState: PerspectiveUiState,
    onBackClick: () -> Unit,
    onNextClick: (String) -> Unit,
    onMoreClick: (String, Long) -> Unit,
    scrollToCommentId: String? = null,
    modifier: Modifier = Modifier,
    onSubmitPerspective: (String, () -> Unit) -> Unit,
    onSelectOption: (Long?) -> Unit,
    onUpdateSort: (String) -> Unit,
    onRefreshAllData: () -> Unit,
    onSetEditMode: (Long) -> Unit,
    onDeletePerspective: (Long) -> Unit,
    onReportPerspective: (Long) -> Unit,
    onToggleLike: (Long, Boolean) -> Unit,
    onLoadPerspectives: () -> Unit
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val voteOptions = uiState.voteOptions
    val tabList = remember(voteOptions) {
        listOf("전체") + voteOptions.map { it.title }
    }

    val inputFieldState = rememberTextFieldState()
    val pagerState = rememberPagerState(pageCount = { tabList.size })
    val coroutineScope = rememberCoroutineScope()
    val pullToRefreshState = rememberPullToRefreshState()

    var isRefreshing by remember { mutableStateOf(false) }
    var isSorting by remember { mutableStateOf(false) }
    var perspectiveToDelete by remember { mutableStateOf<Long?>(null) }
    var perspectiveToReport by remember { mutableStateOf<Long?>(null) }
    var scrollToTopTrigger by remember { mutableIntStateOf(0) }
    var hasScrolledToComment by remember { mutableStateOf(false) }

    val isShowingMyPendingOrRejected =
        uiState.myPerspective?.let { it.status != "PUBLISHED" } ?: false

    BackHandler {
        onBackClick()
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
        containerColor = PickeTheme.colors.backgroundBrand,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = uiState.battleTitle.ifBlank { "관점 남기기" },
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = false,
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBrand,
                    actions = {
                        IconButton(onClick = { onNextClick(uiState.battleId) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_arrow_right),
                                contentDescription = "null",
                                tint = PickeTheme.colors.textPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            val isEditing = uiState.editingPerspectiveId != null
            val inputHint = if (isEditing) "수정할 내용을 입력해주세요..." else "의견을 남겨보세요..."

            PerspectiveInputField(
                textFieldState = inputFieldState,
                onSubmit = {
                    onSubmitPerspective(inputFieldState.text.toString()) {
                        inputFieldState.clearText()
                        focusManager.clearFocus()
                        scrollToTopTrigger++
                    }
                },
                hintText = inputHint,
                editingKey = uiState.editingPerspectiveId,
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (voteOptions.isEmpty()) {
                PerspectiveHeaderSkeleton()
                PerspectiveTabBarSkeleton()
            } else {
                PerspectiveHeader(
                    voteOptions = uiState.voteOptions,
                    opinionChanged = uiState.opinionChanged
                )

                CustomTabBar(
                    tabs = tabList,
                    selectedTab = tabList[pagerState.currentPage],
                    isScrollable = tabList.size > 3,
                    onTabSelected = { selected ->
                        val targetIndex = tabList.indexOf(selected)
                        coroutineScope.launch { pagerState.animateScrollToPage(targetIndex) }
                        val optionId =
                            if (targetIndex == 0) null else voteOptions.getOrNull(targetIndex - 1)?.optionId
                        onSelectOption(optionId)
                    }
                )
            }

            LaunchedEffect(pagerState.currentPage) {
                val optionId =
                    if (pagerState.currentPage == 0) null else voteOptions.getOrNull(pagerState.currentPage - 1)?.optionId
                onSelectOption(optionId)
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.Top
            ) { pageIndex ->
                val listState =
                    remember(uiState.sort, uiState.selectedOptionId, pageIndex) { LazyListState() }
                val filteredList = uiState.perspectives

                LaunchedEffect(scrollToTopTrigger) {
                    if (scrollToTopTrigger > 0) {
                        kotlinx.coroutines.delay(50)
                        listState.scrollToItem(0)
                    }
                }

                if (pageIndex == 0 && scrollToCommentId != null) {
                    LaunchedEffect(uiState.perspectives) {
                        if (!hasScrolledToComment && uiState.perspectives.isNotEmpty() && !uiState.isLoading) {
                            val targetIndex =
                                uiState.perspectives.indexOfFirst { it.commentId == scrollToCommentId }
                            if (targetIndex >= 0) {
                                val offset = if (isShowingMyPendingOrRejected) 1 else 0
                                listState.animateScrollToItem(targetIndex + offset)
                                hasScrolledToComment = true
                            }
                        }
                    }
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
                            onClick = {
                                if (uiState.sort != "popular") {
                                    isSorting = true
                                    onUpdateSort("popular")
                                }
                            }
                        )
                        SortFilterChip(
                            text = "최신순",
                            isSelected = uiState.sort == "latest",
                            onClick = {
                                if (uiState.sort != "latest") {
                                    isSorting = true
                                    onUpdateSort("latest")
                                }
                            }
                        )
                    }

                    if (uiState.isLoading && uiState.perspectives.isEmpty()) {
                        PerspectiveListSkeleton(modifier = Modifier.weight(1f))
                    } else {
                        PullToRefreshBox(
                            state = pullToRefreshState,
                            isRefreshing = isRefreshing,
                            onRefresh = {
                                isRefreshing = true
                                onRefreshAllData()
                            },
                            modifier = Modifier.weight(1f),
                            indicator = {
                                PullToRefreshDefaults.Indicator(
                                    state = pullToRefreshState,
                                    isRefreshing = isRefreshing,
                                    containerColor = Color.White,
                                    color = PickeTheme.colors.primary,
                                    modifier = Modifier.align(Alignment.TopCenter)
                                )
                            }
                        ) {
                            LazyColumn(
                                state = listState,
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(
                                    horizontal = 16.dp,
                                    vertical = 12.dp
                                ),
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
                                                status = myView.status,
                                                clickable = false,
                                                onEditClick = {
                                                    inputFieldState.setTextAndPlaceCursorAtEnd(it)
                                                    onSetEditMode(myView.perspectiveId)
                                                },
                                                onDeleteClick = {
                                                    perspectiveToDelete = myView.perspectiveId
                                                },
                                                onLikeClick = {
                                                    Toast.makeText(
                                                        context,
                                                        "본인이 쓴 관점에는 좋아요를 누를 수 없습니다.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                            )
                                        }
                                    }
                                }

                                if (filteredList.isEmpty() && !uiState.isLoading && !isShowingMyPendingOrRejected) {
                                    item {
                                        val emptyMsg = if (pageIndex == 0) {
                                            "아직 작성된 관점이 없습니다"
                                        } else {
                                            "아직 작성된 \"${tabList.getOrElse(pageIndex) { "" }}\" 관점이 없습니다"
                                        }

                                        PerspectiveEmptyState(
                                            message = emptyMsg,
                                            modifier = Modifier.fillParentMaxSize()
                                        )
                                    }
                                } else {
                                    itemsIndexed(
                                        items = filteredList,
                                        key = { _, item -> item.commentId }
                                    ) { index, item ->
                                        PerspectiveItemCard(
                                            item = item,
                                            onMoreClick = {
                                                onMoreClick(
                                                    item.commentId,
                                                    uiState.voteOptions.firstOrNull()
                                                        ?.optionId ?: 0L
                                                )
                                            },
                                            onEditClick = { content ->
                                                inputFieldState.setTextAndPlaceCursorAtEnd(content)
                                                onSetEditMode(item.commentId.toLongOrNull() ?: 0L)
                                            },
                                            onDeleteClick = {
                                                perspectiveToDelete =
                                                    item.commentId.toLongOrNull() ?: 0L
                                            },
                                            onReportClick = {
                                                perspectiveToReport =
                                                    item.commentId.toLongOrNull() ?: 0L
                                            },
                                            onLikeClick = {
                                                if (item.isMine) {
                                                    Toast.makeText(
                                                        context,
                                                        "본인이 쓴 관점에는 좋아요를 누를 수 없습니다.",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                } else {
                                                    onToggleLike(
                                                        item.commentId.toLongOrNull() ?: 0L,
                                                        item.isLiked
                                                    )
                                                }
                                            },
                                        )
                                        val isAtEnd = index == filteredList.lastIndex
                                        val isNotLoading = !uiState.isLoading
                                        val hasMorePages = uiState.hasNext

                                        if (isAtEnd && isNotLoading && hasMorePages) {
                                            LaunchedEffect(item.commentId) {
                                                onLoadPerspectives()
                                            }
                                        }
                                    }
                                }

                                if (uiState.isLoading && !isRefreshing) {
                                    item {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator(color = PickeTheme.colors.primaryDarkest)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (perspectiveToDelete != null) {
            CustomConfirmDialog(
                message = "관점을 삭제하시겠습니까?",
                confirmText = "삭제하기",
                dismissText = "뒤로가기",
                onConfirm = {
                    onDeletePerspective(perspectiveToDelete!!)
                    perspectiveToDelete = null
                },
                onDismiss = {
                    perspectiveToDelete = null
                }
            )
        }

        if (perspectiveToReport != null) {
            CustomConfirmDialog(
                message = "관점을 신고하시겠습니까?",
                confirmText = "신고하기",
                dismissText = "뒤로가기",
                onConfirm = {
                    onReportPerspective(perspectiveToReport!!)
                    perspectiveToReport = null
                },
                onDismiss = {
                    perspectiveToReport = null
                }
            )
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
                text = leftOption?.title ?: "",
                style = PickeTheme.typography.labelXSmall,
                color = PickeTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${proRatio.toInt()}%",
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.neutral600
            )
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f)
        ) {
            Surface(
                color = PickeTheme.colors.primaryLight,
                shape = RoundedCornerShape(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_think),
                        contentDescription = "생각 변경",
                        tint = PickeTheme.colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (opinionChanged) "생각이 바뀌었어요" else "생각이 동일해요",
                        style = PickeTheme.typography.caption2SemiBold,
                        color = PickeTheme.colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
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
                        .background(PickeTheme.colors.backgroundTertiary)
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
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
                text = rightOption?.title ?: "",
                style = PickeTheme.typography.labelXSmall,
                color = PickeTheme.colors.textSecondary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "${conRatio.toInt()}%",
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.neutral600
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PerspectiveScreenPreview() {
    PickeTheme {
        PerspectiveScreenContent(
            uiState = PerspectiveUiState(
                voteOptions = DummyData.dummyVoteOptions,
                perspectives = DummyData.dummyPerspectives
            ),
            onBackClick = {},
            onNextClick = {},
            onMoreClick = { _, _ -> },
            scrollToCommentId = null,
            onSubmitPerspective = { _, _ -> },
            onSelectOption = {},
            onUpdateSort = {},
            onRefreshAllData = {},
            onSetEditMode = {},
            onDeletePerspective = {},
            onReportPerspective = {},
            onToggleLike = { _, _ -> },
            onLoadPerspectives = {}
        )
    }
}