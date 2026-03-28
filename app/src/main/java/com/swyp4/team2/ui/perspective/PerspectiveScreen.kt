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
import com.swyp4.team2.R
import com.swyp4.team2.ui.component.CustomTabBar
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.component.SortFilterChip
import com.swyp4.team2.ui.perspective.model.PerspectiveUiModel
import com.swyp4.team2.ui.theme.Beige200
import com.swyp4.team2.ui.theme.Beige600
import com.swyp4.team2.ui.theme.Gray300
import com.swyp4.team2.ui.theme.Gray600
import com.swyp4.team2.ui.theme.Gray700
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary50
import com.swyp4.team2.ui.theme.Primary500
import com.swyp4.team2.ui.theme.Primary600
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

    var selectedSort by remember { mutableStateOf("인기순") }
    val tabList = listOf("전체", "찬성", "반대")

    var inputText by remember { mutableStateOf("") }

    val pagerState = rememberPagerState(pageCount = { tabList.size })
    val coroutineScope = rememberCoroutineScope()

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
                    onSubmit = { /* TODO: 뷰모델에 댓글 전송 로직 연결 */ }
                )
            }
        }
    ){ innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ){
            PerspectiveHeader(
                proPercentage = uiState.proRatio * 100f,
                conPercentage = uiState.conRatio * 100f
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

            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { pageIndex ->
                val filteredList = when (pageIndex) {
                    1 -> uiState.perspectives.filter { it.stance.label == "찬성" } // 찬성 탭
                    2 -> uiState.perspectives.filter { it.stance.label == "반대" } // 반대 탭
                    else -> uiState.perspectives // 전체 탭
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(filteredList) { index, item ->
                        PerspectiveItemCard(
                            item = item,
                            onMoreClick = { onMoreClick(item.commentId) }
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
                                CircularProgressIndicator(color = Gray900)
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
    isDetail: Boolean = false,
    modifier: Modifier = Modifier,
    onMoreClick: () -> Unit = {},
    clickable: Boolean = true
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

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
                Image(
                    painter = painterResource(id = item.profileImageRes),
                    contentDescription = "프로필 이미지",
                    modifier = Modifier.size(32.dp).clip(CircleShape)
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
                                text = item.stance.label,
                                style = SwypTheme.typography.b5Medium,
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

                // 케밥 메뉴 (기존과 동일)
                Box {
                    IconButton(
                        onClick = { if (clickable) isMenuExpanded = true else isMenuExpanded = false },
                        modifier = Modifier.size(16.dp)
                    ) {
                        Icon(painterResource(id = R.drawable.ic_more), "더보기", tint = Gray300)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                        modifier = Modifier.background(Primary600).clip(RoundedCornerShape(8.dp))
                    ) {
                        PerspectiveMenuItem(iconRes = R.drawable.ic_trash, text = "삭제") { isMenuExpanded = false }
                        PerspectiveMenuItem(iconRes = R.drawable.ic_edit, text = "수정") { isMenuExpanded = false }
                        PerspectiveMenuItem(iconRes = R.drawable.ic_bell, text = "신고") { isMenuExpanded = false }
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
                    Icon(painterResource(id = R.drawable.ic_message), "댓글", Modifier.size(12.dp), tint = Gray300)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("${item.replyCount}", style = SwypTheme.typography.b5Medium, color = Gray300)
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Icon(painterResource(id = R.drawable.ic_heart_plus), "좋아요", Modifier.size(12.dp), tint = Gray300)
                Spacer(modifier = Modifier.width(4.dp))
                Text("${item.likeCount}", style = SwypTheme.typography.b5Medium, color = Gray300)
            }
        }
    }
}

// 🌟 커스텀 메뉴 아이템을 그려주는 공통 컴포저블
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
                // 시안과 비슷한 연한 붉은 배경색 (나중에 SwypTheme.colors 설정에 맞춰 변경하세요!)
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
                        tint = Primary500, // 텍스트와 같은 진한 붉은색
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "생각이 바뀌었어요",
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