package com.picke.app.ui.comment

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
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
import androidx.compose.ui.graphics.Color
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
import com.picke.app.ui.theme.Beige100
import com.picke.app.ui.theme.Beige200
import com.picke.app.ui.theme.Beige600
import com.picke.app.ui.theme.Gray300
import com.picke.app.ui.theme.Gray600
import com.picke.app.ui.theme.Gray700
import com.picke.app.ui.theme.Gray900
import com.picke.app.ui.theme.Primary500
import com.picke.app.ui.theme.Primary600
import com.picke.app.ui.theme.Primary900
import com.picke.app.ui.theme.SwypTheme
import kotlinx.coroutines.flow.collectLatest

@Composable
fun CommentScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = androidx.compose.ui.platform.LocalContext.current
    var inputText by remember { mutableStateOf("") }

    var commentToDelete by remember { mutableStateOf<Long?>(null) }
    var commentToReport by remember { mutableStateOf<Long?>(null) }

    val pullToRefreshState = rememberPullToRefreshState()
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
        containerColor = Beige200,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "댓글 남기기",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = Beige200,

                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                val isEditing = uiState.editingCommentId != null
                val inputHint = if (isEditing) "수정할 내용을 입력해주세요..." else "댓글을 남겨보세요..."

                CommentInputField(
                    inputText = inputText,
                    onTextChanged = { inputText = it },
                    onSubmit = {
                        viewModel.submitComment(inputText) {
                            inputText = ""
                        }
                    },
                    hintText = inputHint
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 로딩중 일때 (본문도 없고 댓글도 없을 때)
            if (uiState.isLoading && uiState.comments.isEmpty() && uiState.mainPerspective == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary900)
                }
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
                            color = Primary500,
                            modifier = Modifier.align(Alignment.TopCenter)
                        )
                    }
                ) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // 3) 실제 댓글 리스트
                        items(uiState.comments) { comment ->
                            CommentItemCard(
                                item = comment,
                                onEditClick = { content ->
                                    inputText = content
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
                                color = Beige600,
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
            color = Gray600
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
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = if (item.isMine) "나" else item.nickname, style = SwypTheme.typography.labelMedium, color = Gray700)
                    Spacer(modifier = Modifier.width(6.dp))

                    val isPro = item.stance == "찬성"
                    val badgeBgColor = if (isPro) Beige600 else SwypTheme.colors.primary
                    val badgeTextColor = if (isPro) SwypTheme.colors.primary else Beige600

                    Box(
                        modifier = Modifier
                            .background(badgeBgColor, RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = item.stance, style = SwypTheme.typography.b5Medium, color = badgeTextColor)
                    }
                }
                Text(text = item.timeAgo, style = SwypTheme.typography.labelXSmall, color = SwypTheme.colors.outline)
            }

            // 원본 글이 아닐 때만 케밥 메뉴 띄우기
            if (!isMainContent) {
                Box {
                    IconButton(onClick = { isMenuExpanded = true }, modifier = Modifier.size(16.dp)) {
                        Icon(painterResource(id = R.drawable.ic_more), "더보기", tint = Gray300)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.background(Primary600).clip(RoundedCornerShape(8.dp))
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

        Spacer(modifier = Modifier.height(12.dp))

        Text(text = item.content, style = SwypTheme.typography.b4Regular, color = Gray600)

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
                        tint = if (item.isLiked) SwypTheme.colors.primary else Gray300
                    )
                }

                Text(
                    text = "${item.likeCount}",
                    style = SwypTheme.typography.b5Medium,
                    color = if (item.isLiked) SwypTheme.colors.primary else Gray300,
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
    inputText: String,
    onTextChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    isEnabled: Boolean = true,
    hintText: String = "댓글을 남겨보세요...",
    modifier: Modifier = Modifier
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
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(if (isEnabled) Beige200 else Beige100, RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
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
                    textStyle = SwypTheme.typography.b3Regular.copy(color = Gray900, lineHeight = 20.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp)
                )

                Text(
                    text = "${inputText.length}/200",
                    style = SwypTheme.typography.labelXSmall,
                    color = SwypTheme.colors.outline,
                    modifier = Modifier.align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

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