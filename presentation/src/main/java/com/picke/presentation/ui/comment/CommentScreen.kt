package com.picke.presentation.ui.comment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.picke.presentation.ui.comment.component.CommentInputField
import com.picke.presentation.ui.comment.component.CommentItemCard
import com.picke.presentation.ui.comment.component.CommentSkeleton
import com.picke.presentation.ui.comment.model.CommentUiEvent
import com.picke.presentation.ui.comment.model.CommentUiModel
import com.picke.presentation.ui.comment.model.CommentUiState
import com.picke.presentation.ui.component.CustomConfirmDialog
import com.picke.presentation.ui.component.CustomTopAppBar
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommentScreen(
    onBackClick: () -> Unit,
    scrollToCommentId: String? = null,
    modifier: Modifier = Modifier,
    viewModel: CommentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val inputFieldState = rememberTextFieldState()

    var commentToDelete by remember { mutableStateOf<Long?>(null) }
    var commentToReport by remember { mutableStateOf<Long?>(null) }
    var isRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRefreshing = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collectLatest { event ->
            when (event) {
                is CommentUiEvent.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    CommentScreenContent(
        uiState = uiState,
        scrollToCommentId = scrollToCommentId,
        modifier = modifier,
        inputFieldState = inputFieldState,
        isRefreshing = isRefreshing,
        commentToDelete = commentToDelete,
        commentToReport = commentToReport,
        onBackClick = onBackClick,
        onRefresh = {
            isRefreshing = true
            viewModel.refreshAllData()
        },
        onSubmit = {
            viewModel.submitComment(inputFieldState.text.toString()) {
                inputFieldState.clearText()
            }
        },
        onMainLikeClick = { mainContent ->
            if (mainContent.isMine) {
                Toast.makeText(context, "본인이 쓴 관점에는 좋아요를 누를 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.toggleMainPerspectiveLike()
            }
        },
        onEditComment = { commentId, content ->
            inputFieldState.setTextAndPlaceCursorAtEnd(content)
            viewModel.setEditMode(commentId)
        },
        onRequestDelete = { commentId ->
            commentToDelete = commentId
        },
        onRequestReport = { commentId ->
            commentToReport = commentId
        },
        onLikeComment = { commentId, isLiked, isMine ->
            if (isMine) {
                Toast.makeText(context, "본인이 쓴 댓글에는 좋아요를 누를 수 없습니다.", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.toggleLike(commentId = commentId, isCurrentlyLiked = isLiked)
            }
        },
        onConfirmDelete = {
            commentToDelete?.let { viewModel.deleteComment(it) }
            commentToDelete = null
        },
        onDismissDelete = {
            commentToDelete = null
        },
        onConfirmReport = {
            commentToReport?.let { viewModel.reportComment(it) }
            commentToReport = null
        },
        onDismissReport = {
            commentToReport = null
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentScreenContent(
    uiState: CommentUiState,
    scrollToCommentId: String?,
    modifier: Modifier = Modifier,
    inputFieldState: TextFieldState,
    isRefreshing: Boolean,
    commentToDelete: Long?,
    commentToReport: Long?,
    onBackClick: () -> Unit,
    onRefresh: () -> Unit,
    onSubmit: () -> Unit,
    onMainLikeClick: (CommentUiModel) -> Unit,
    onEditComment: (Long, String) -> Unit,
    onRequestDelete: (Long) -> Unit,
    onRequestReport: (Long) -> Unit,
    onLikeComment: (Long, Boolean, Boolean) -> Unit,
    onConfirmDelete: () -> Unit,
    onDismissDelete: () -> Unit,
    onConfirmReport: () -> Unit,
    onDismissReport: () -> Unit
) {
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()

    LaunchedEffect(scrollToCommentId, uiState.comments) {
        if (scrollToCommentId != null && uiState.comments.isNotEmpty()) {
            val targetIndex = uiState.comments.indexOfFirst { it.commentId == scrollToCommentId }
            if (targetIndex >= 0) listState.animateScrollToItem(targetIndex)
        }
    }

    Scaffold(
        containerColor = PickeTheme.colors.backgroundBrand,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "댓글",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = PickeTheme.colors.backgroundBrand
                )
            }
        },
        bottomBar = {
            val isEditing = uiState.editingCommentId != null
            val inputHint = if (isEditing) "수정할 내용을 입력해주세요..." else "댓글을 남겨보세요..."

            CommentInputField(
                textFieldState = inputFieldState,
                onSubmit = onSubmit,
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
            if (uiState.isLoading && uiState.comments.isEmpty() && uiState.mainPerspective == null) {
                CommentSkeleton(modifier = Modifier.fillMaxSize())
            } else {
                uiState.mainPerspective?.let { mainContent ->
                    CommentItemCard(
                        item = mainContent,
                        isMainContent = true,
                        onLikeClick = { onMainLikeClick(mainContent) }
                    )
                }

                CommentHeaderSection(count = uiState.comments.size)

                PullToRefreshBox(
                    state = pullToRefreshState,
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
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
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.comments) { comment ->
                            val commentIdLong = comment.commentId.toLongOrNull() ?: 0L
                            CommentItemCard(
                                item = comment,
                                onEditClick = { content ->
                                    onEditComment(commentIdLong, content)
                                },
                                onDeleteClick = {
                                    onRequestDelete(commentIdLong)
                                },
                                onReportClick = {
                                    onRequestReport(commentIdLong)
                                },
                                onLikeClick = {
                                    onLikeComment(commentIdLong, comment.isLiked, comment.isMine)
                                }
                            )
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = PickeTheme.colors.borderDefault,
                            )
                        }
                    }
                }
            }
        }

        if (commentToDelete != null) {
            CustomConfirmDialog(
                message = "댓글을 삭제하시겠습니까?",
                confirmText = "삭제하기",
                dismissText = "뒤로가기",
                onConfirm = onConfirmDelete,
                onDismiss = onDismissDelete
            )
        }

        if (commentToReport != null) {
            CustomConfirmDialog(
                message = "댓글을 신고하시겠습니까?",
                confirmText = "신고하기",
                dismissText = "뒤로가기",
                onConfirm = onConfirmReport,
                onDismiss = onDismissReport
            )
        }
    }
}

@Composable
fun CommentHeaderSection(count: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F8F6))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Text(
            text = "답글 ${count}개",
            style = PickeTheme.typography.b4Regular.copy(fontWeight = FontWeight.SemiBold),
            color = PickeTheme.colors.neutral600
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CommentScreenPreview() {
    PickeTheme {
        CommentScreenContent(
            uiState = CommentUiState(comments = DummyData.dummyComments),
            scrollToCommentId = null,
            inputFieldState = rememberTextFieldState(),
            isRefreshing = false,
            commentToDelete = null,
            commentToReport = null,
            onBackClick = {},
            onRefresh = {},
            onSubmit = {},
            onMainLikeClick = {},
            onEditComment = { _, _ -> },
            onRequestDelete = {},
            onRequestReport = {},
            onLikeComment = { _, _, _ -> },
            onConfirmDelete = {},
            onDismissDelete = {},
            onConfirmReport = {},
            onDismissReport = {}
        )
    }
}