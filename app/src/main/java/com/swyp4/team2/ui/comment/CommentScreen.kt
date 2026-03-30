package com.swyp4.team2.ui.comment

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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary600
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun CommentScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CommentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = SwypTheme.colors.surface,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "댓글",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.surface,
                )
            }
        },
        bottomBar = {
            Box(modifier = Modifier.navigationBarsPadding()) {
                CommentInputField(
                    inputText = inputText,
                    onTextChanged = { inputText = it },
                    onSubmit = {
                        viewModel.submitComment(inputText) {
                            inputText = "" // 성공하면 비우기
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(innerPadding)
        ) {
            // 2. 답글 N개 헤더 띠
            item {
                CommentHeader(count = uiState.comments.size)
            }

            // 3. 댓글 리스트 영역
            items(uiState.comments) { comment ->
                CommentItemCard(
                    item = comment,
                    onEditClick = { content ->
                        inputText = content
                        viewModel.setEditMode(comment.commentId.toLongOrNull())
                    },
                    onDeleteClick = {
                        viewModel.deleteComment(comment.commentId.toLongOrNull() ?: 0L)
                    },
                    onLikeClick = {
                        viewModel.toggleLike(
                            commentId = comment.commentId.toLongOrNull() ?: 0L,
                            isCurrentlyLiked = comment.isLiked
                        )
                    }
                )
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF0F0F0)))
            }
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
            style = SwypTheme.typography.h4SemiBold,
            color = Gray900
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
    onLikeClick: () -> Unit = {}
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = item.profileImageUrl,
                contentDescription = "프로필 이미지",
                modifier = Modifier.size(32.dp).clip(CircleShape),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.ic_profile_mengzi),
                error = painterResource(id = R.drawable.ic_profile_mengzi)
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = item.nickname, style = SwypTheme.typography.labelMedium, color = Gray700)
                    Spacer(modifier = Modifier.width(6.dp))
                    Box(
                        modifier = Modifier
                            .background(SwypTheme.colors.primary.copy(alpha = 0.1f), RoundedCornerShape(2.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(text = item.stance, style = SwypTheme.typography.b5Medium, color = SwypTheme.colors.primary)
                    }
                }
                Text(text = item.timeAgo, style = SwypTheme.typography.labelXSmall, color = SwypTheme.colors.outline)
            }

            // 원본 글(MainContent)이 아닐 때만 케밥 메뉴 띄우기
            if (!isMainContent) {
                Box {
                    IconButton(onClick = { isMenuExpanded = true }, modifier = Modifier.size(24.dp)) {
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
                        }
                        CommentMenuItem(iconRes = R.drawable.ic_bell, text = "신고") { isMenuExpanded = false }
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
                modifier = Modifier.clickable { onLikeClick() }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_plus),
                    contentDescription = "좋아요",
                    modifier = Modifier.size(16.dp),
                    tint = if (item.isLiked) SwypTheme.colors.primary else Gray300
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.likeCount}",
                    style = SwypTheme.typography.b5Medium,
                    color = if (item.isLiked) SwypTheme.colors.primary else Gray300
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
                    .background(Color(0xFFFBF9F6), RoundedCornerShape(8.dp))
                    .padding(12.dp)
            ) {
                if (inputText.isEmpty()) {
                    Text(
                        text = "댓글을 남겨보세요...",
                        style = SwypTheme.typography.b3Regular,
                        color = SwypTheme.colors.outline,
                        lineHeight = 20.sp
                    )
                }

                BasicTextField(
                    value = inputText,
                    onValueChange = { if (it.length <= 200) onTextChanged(it) },
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