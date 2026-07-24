package com.picke.app.ui.comment

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.app.R
import com.picke.app.ui.component.CustomConfirmDialog
import com.picke.app.ui.component.CustomTopAppBar
import com.picke.app.ui.component.ProfileImage
import com.picke.app.ui.theme.SwypTheme
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreen(
    onBackClick: () -> Unit,
    scrollToCommentId: String? = null,
    modifier: Modifier = Modifier,
    viewModel: CommentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    // TextFieldState: 프로그램적으로 텍스트를 비우거나(clearText) 채울 때(setTextAndPlaceCursorAtEnd)
    // IME 조합 상태와의 경쟁 없이 안전하게 처리해주는 최신 API.
    val inputFieldState = rememberTextFieldState()

    var commentToDelete by remember { mutableStateOf<Long?>(null) }
    var commentToReport by remember { mutableStateOf<Long?>(null) }

    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(scrollToCommentId, uiState.comments) {
        if (scrollToCommentId != null && uiState.comments.isNotEmpty()) {
            val targetIndex = uiState.comments.indexOfFirst { it.commentId == scrollToCommentId }
            if (targetIndex >= 0) listState.animateScrollToItem(targetIndex)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is CommentUiEvent.ShowToast -> {
                    android.widget.Toast.makeText(
                        context,
                        event.message,
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    Scaffold(
        containerColor = SwypTheme.colors.backgroundBrand,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "댓글",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.backgroundBrand,

                )
            }
        },
        bottomBar = {
            val isEditing = uiState.editingCommentId != null
            val inputHint = if (isEditing) "수정할 내용을 입력해주세요..." else "댓글을 남겨보세요..."

            CommentInputField(
                textFieldState = inputFieldState,
                onSubmit = {
                    viewModel.submitComment(inputFieldState.text.toString()) {
                        inputFieldState.clearText()
                    }
                },
                hintText = inputHint,
                editingKey = uiState.editingCommentId
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 로딩중 일때 (본문도 없고 댓글도 없을 때)
            if (uiState.isLoading && uiState.comments.isEmpty() && uiState.mainPerspective == null) {
                CommentSkeleton(modifier = Modifier.fillMaxSize())
            } else {
                // 2. 상단 고정 영역 (스크롤 되지 않음)
                // 1) 메인 관점 카드
                uiState.mainPerspective?.let { mainContent ->
                    CommentItemCard(
                        item = mainContent,
                        isMainContent = true,
                        onLikeClick = {
                            if (mainContent.isMine) {
                                android.widget.Toast.makeText(
                                    context,
                                    "본인이 쓴 관점에는 좋아요를 누를 수 없습니다.",
                                    android.widget.Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                viewModel.toggleMainPerspectiveLike()
                            }
                        }
                    )
                }

                // 2) 답글 N개 헤더 띠
                CommentHeader(count = uiState.comments.size)

                // 3. 스크롤 & 새로고침 영역 (댓글 목록 전용)
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
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 3) 실제 댓글 리스트
                        items(uiState.comments) { comment ->
                            CommentItemCard(
                                item = comment,
                                onEditClick = { content ->
                                    inputFieldState.setTextAndPlaceCursorAtEnd(content)
                                    viewModel.setEditMode(comment.commentId.toLongOrNull())
                                },
                                onDeleteClick = {
                                    commentToDelete = comment.commentId.toLongOrNull() ?: 0L
                                },
                                onReportClick = {
                                    commentToReport = comment.commentId.toLongOrNull() ?: 0L
                                },
                                onLikeClick = {
                                    if (comment.isMine) {
                                        android.widget.Toast.makeText(
                                            context,
                                            "본인이 쓴 댓글에는 좋아요를 누를 수 없습니다.",
                                            android.widget.Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        viewModel.toggleLike(
                                            commentId = comment.commentId.toLongOrNull() ?: 0L,
                                            isCurrentlyLiked = comment.isLiked
                                        )
                                    }
                                }
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = SwypTheme.colors.borderDefault,
                            )
                        }
                    }
                }
            }
        }

        // 삭제 다이얼로그 호출
        if (commentToDelete != null) {
            CustomConfirmDialog(
                message = "댓글을 삭제하시겠습니까?",
                confirmText = "삭제하기",
                dismissText = "뒤로가기",
                onConfirm = {
                    viewModel.deleteComment(commentToDelete!!)
                    commentToDelete = null
                },
                onDismiss = {
                    commentToDelete = null
                }
            )
        }

        // 신고 다이얼로그 호출
        if (commentToReport != null) {
            CustomConfirmDialog(
                message = "댓글을 신고하시겠습니까?",
                confirmText = "신고하기",
                dismissText = "뒤로가기",
                onConfirm = {
                    viewModel.reportComment(commentToReport!!)
                    commentToReport = null
                },
                onDismiss = {
                    commentToReport = null
                }
            )
        }
    }
}

@Composable
fun CommentHeader(count: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F8F6))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "답글 ${count}개",
            style = SwypTheme.typography.b4Regular.copy(fontWeight = FontWeight.SemiBold),
            color = SwypTheme.colors.neutral600
        )
    }
}

@Composable
fun CommentItemCard(
    item: CommentUiModel,
    modifier: Modifier = Modifier,
    isMainContent: Boolean = false,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onLikeClick: () -> Unit = {},
    onReportClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileImage(
                model = item.profileImageUrl,
                modifier = Modifier.size(32.dp).clip(CircleShape),
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = if (item.isMine) "나" else item.nickname, style = SwypTheme.typography.labelMedium, color = SwypTheme.colors.textSecondary)
                Text(text = item.timeAgo, style = SwypTheme.typography.labelXSmall, color = SwypTheme.colors.outline)
            }

            // 원본 글이 아닐 때만 케밥 메뉴 띄우기
            if (!isMainContent) {
                Box {
                    IconButton(onClick = { isMenuExpanded = true }, modifier = Modifier.size(16.dp)) {
                        Icon(painterResource(id = R.drawable.ic_more), "더보기", tint = SwypTheme.colors.textMuted)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.background(SwypTheme.colors.primaryPressed).clip(RoundedCornerShape(8.dp))
                    ) {
                        if (item.isMine) {
                            CommentMenuItem(iconRes = R.drawable.ic_trash, text = "삭제") {
                                isMenuExpanded = false
                                onDeleteClick()
                            }
                            CommentMenuItem(iconRes = R.drawable.ic_edit, text = "수정") {
                                isMenuExpanded = false
                                onEditClick(item.content)
                            }
                        } else {
                            CommentMenuItem(iconRes = R.drawable.ic_bell, text = "신고") {
                                isMenuExpanded = false
                                onReportClick()
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 입장 뱃지
        Surface(
            color = SwypTheme.colors.badgeBackground,
            shape = RoundedCornerShape(2.dp)
        ) {
            Text(
                text = item.stance,
                style = SwypTheme.typography.b5Medium,
                color = SwypTheme.colors.badgeText,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = item.content, style = SwypTheme.typography.b4Regular, color = SwypTheme.colors.neutral600)

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                        .size(30.dp)
                        .clip(CircleShape)
                        .clickable { onLikeClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart_plus),
                        contentDescription = "좋아요",
                        modifier = Modifier.size(16.dp),
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

@Composable
fun CommentMenuItem(
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
        Text(text = text, style = SwypTheme.typography.labelMedium, color = Color.White)
    }
}

@Composable
fun CommentInputField(
    textFieldState: TextFieldState,
    onSubmit: () -> Unit,
    isEnabled: Boolean = true,
    hintText: String = "댓글을 남겨보세요...",
    modifier: Modifier = Modifier,
    editingKey: Any? = null,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(editingKey) {
        if (editingKey != null) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Surface(
        color = SwypTheme.colors.surfaceTertiary,
        shadowElevation = 16.dp,
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .imePadding()
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (isEnabled) SwypTheme.colors.surface else SwypTheme.colors.beige100, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                if (textFieldState.text.isEmpty()) {
                    Text(
                        text = hintText,
                        style = SwypTheme.typography.b3Regular,
                        color = SwypTheme.colors.outline,
                        lineHeight = 20.sp
                    )
                }

                BasicTextField(
                    state = textFieldState,
                    enabled = isEnabled,
                    lineLimits = TextFieldLineLimits.MultiLine(minHeightInLines = 3, maxHeightInLines = Int.MAX_VALUE),
                    textStyle = SwypTheme.typography.b3Regular.copy(color = SwypTheme.colors.textPrimary, lineHeight = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (isEnabled) SwypTheme.colors.buttonPrimaryBackground else SwypTheme.colors.buttonPrimaryBackgroundDisabled)
                    .clickable(enabled = isEnabled) { onSubmit() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "등록",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}