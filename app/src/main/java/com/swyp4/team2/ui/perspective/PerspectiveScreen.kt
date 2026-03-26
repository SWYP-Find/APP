package com.swyp4.team2.ui.perspective

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.SortFilterChip
import com.swyp4.team2.ui.perspective.model.PerspectiveUiModel
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun PerspectiveScreen(
    onBackClick: ()->Unit,
    onNextClick: ()->Unit,
    modifier: Modifier = Modifier,
    viewModel: PerspectiveViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableStateOf("전체") }
    var selectedSort by remember { mutableStateOf("인기순") }

    val tabList = listOf("전체", "찬성", "반대")

    var inputText by remember { mutableStateOf("") }

    Scaffold(
        containerColor = SwypTheme.colors.surface,
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()) {
                CustomTopAppBar(
                    title = "배틀 제목",
                    centerTitle = true,
                    showLogo = false,
                    showBackButton = true,
                    onBackClick = onBackClick,
                    backgroundColor = SwypTheme.colors.surface,
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
            PerspectiveInputField(
                inputText = inputText,
                onTextChanged = { inputText = it },
                onSubmit = { /* TODO: 뷰모델에 댓글 전송 로직 연결 */ }
            )
        }
    ){ innerPadding ->
        Column(
            modifier = modifier.fillMaxSize()
                .padding(innerPadding)
        ){
            CustomTabBar(
                tabs = tabList,
                selectedTab = selectedTab,
                isScrollable = false,
                onTabSelected = { selectedTab = it }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SortFilterChip(
                    text = "인기순",
                    isSelected = selectedSort == "인기순",
                    onClick = { selectedSort = "인기순" }
                )

                SortFilterChip(
                    text = "최신순",
                    isSelected = selectedSort == "최신순",
                    onClick = { selectedSort = "최신순" }
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(uiState.perspectives) { index, item ->
                    PerspectiveItemCard(item = item)

                    if (index == uiState.perspectives.lastIndex && uiState.hasNext && !uiState.isLoading) {
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
                            CircularProgressIndicator(color = Gray900)
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
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // 그림자 없음
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // 1. 프로필 영역 (이미지 + 닉네임 + 딱지 + 케밥 메뉴)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = item.profileImageRes),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier.size(36.dp).clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(8.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = item.nickname,
                            style = SwypTheme.typography.h4SemiBold,
                            color = Gray900
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        // 찬성/반대 딱지 (연한 배경 + 진한 글씨)
                        Surface(
                            color = SwypTheme.colors.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text = item.stance.label,
                                style = SwypTheme.typography.labelXSmall,
                                color = SwypTheme.colors.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = item.timeAgo,
                        style = SwypTheme.typography.labelXSmall,
                        color = SwypTheme.colors.outline
                    )
                }

                IconButton(
                    onClick = { /* TODO: 케밥 메뉴 클릭 (수정/삭제/신고) */ },
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_more),
                        contentDescription = "더보기",
                        tint = SwypTheme.colors.outline
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. 본문 영역 (최대 3줄)
            Text(
                text = item.content,
                style = SwypTheme.typography.b3Regular,
                color = Gray900,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 3. 하단 영역 (더보기 버튼 + 댓글 수 + 좋아요 수)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "더보기",
                    style = SwypTheme.typography.labelMedium,
                    color = SwypTheme.colors.outline
                )

                Spacer(modifier = Modifier.weight(1f))

                // 댓글 갯수
                Icon(
                    painter = painterResource(id = R.drawable.ic_message),
                    contentDescription = "댓글",
                    modifier = Modifier.size(16.dp),
                    tint = SwypTheme.colors.outline
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.replyCount}",
                    style = SwypTheme.typography.labelMedium,
                    color = SwypTheme.colors.outline
                )

                Spacer(modifier = Modifier.width(12.dp))

                // 좋아요 갯수
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_plus),
                    contentDescription = "좋아요",
                    modifier = Modifier.size(16.dp),
                    tint = SwypTheme.colors.outline
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.likeCount}",
                    style = SwypTheme.typography.labelMedium,
                    color = SwypTheme.colors.outline
                )
            }
        }
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
        modifier = modifier.fillMaxWidth().imePadding()
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
                    modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp) // 글자수 카운터 공간 확보
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
                    // TODO: 디자이너님이 주신 마법봉 아이콘으로 교체!
                    painter = painterResource(id = android.R.drawable.ic_menu_send),
                    contentDescription = "등록",
                    tint = SwypTheme.colors.primary
                )
            }
        }
    }
}