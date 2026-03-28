package com.swyp4.team2.ui.perspective

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.PerspectiveStance
import com.swyp4.team2.ui.component.CustomTopAppBar
import com.swyp4.team2.ui.perspective.model.PerspectiveUiModel
import com.swyp4.team2.ui.theme.Gray900
import com.swyp4.team2.ui.theme.Primary600
import com.swyp4.team2.ui.theme.SwypTheme

@Composable
fun PerspectiveDetailScreen(
    onBackClick: ()->Unit,
    modifier: Modifier = Modifier,
    mainComment: PerspectiveUiModel = PerspectiveUiModel(
        commentId = 1L,
        profileImageRes = R.drawable.ic_profile_mengzi, // 보내주신 코드의 리소스 반영
        nickname = "사유하는 라쿤",
        stance = PerspectiveStance.AGREE, // 찬성
        content = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.\n\n처음에는 말기 환자만을 위한 제도였지만, 시간이 지나면서 대상이 점점 확대되었고, '합리적 선택'이라는 이름 아래 사회적으로 취약한 사람들이 스스로 짐이 된다고 느끼는 상황이 만들어지고 있습니다.\n\n진정한 자율적 선택이란, 충분한 돌봄과 지원이 보장된 상태에서만 가능합니다. 경제적 부담, 가족에 대한 미안함, 사회적 시선 속에서 내리는 선택을 과연 '자유로운 선택'이라고 할 수 있을까요?",
        timeAgo = "2분 전",
        replyCount = 23,
        likeCount = 1340,
        isLiked = false
    ),
    replies: List<PerspectiveUiModel> = listOf(
        PerspectiveUiModel(
            commentId = 101L,
            profileImageRes = R.drawable.ic_profile_mengzi,
            nickname = "사색하는 사슴",
            stance = PerspectiveStance.DISAGREE, // 반대 의견으로 설정해봤어요!
            content = "네덜란드 사례를 일반화하기엔 무리가 있지 않나요? 한국의 사회문화적 맥락은 다릅니다.",
            timeAgo = "2분 전",
            replyCount = 0,
            likeCount = 1340,
            isLiked = true // 내가 좋아요 누른 상태 예시
        ),
        PerspectiveUiModel(
            commentId = 102L,
            profileImageRes = R.drawable.ic_profile_mengzi,
            nickname = "논쟁하는 사자",
            stance = PerspectiveStance.DISAGREE,
            content = "저도 사슴님 의견에 동의합니다. 제도의 남용을 막을 안전장치를 마련하는 게 우선이죠.",
            timeAgo = "2분 전",
            replyCount = 0,
            likeCount = 12,
            isLiked = false
        ),
        PerspectiveUiModel(
            commentId = 103L,
            profileImageRes = R.drawable.ic_profile_mengzi,
            nickname = "행복한 강아지",
            stance = PerspectiveStance.AGREE,
            content = "하지만 오남용의 위험성보다는 고통받는 이들의 자기결정권이 더 우선시되어야 한다고 생각해요.",
            timeAgo = "1분 전",
            replyCount = 0,
            likeCount = 5,
            isLiked = false
        )
    )
) {
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
                PerspectiveInputField(
                    inputText = inputText,
                    onTextChanged = { inputText = it },
                    onSubmit = { /* TODO: 뷰모델에 댓글 전송 로직 연결 */ }
                )
            }
        }
    ){ innerPadding ->
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. 원본 댓글 영역 (isDetail = true 로 넘겨서 전체 텍스트 노출!)
            item {
                PerspectiveItemCard(
                    item = mainComment,
                    isDetail = true,
                    modifier = Modifier.padding(16.dp)
                )
            }

            // 2. 답글 N개 헤더 띠
            item {
                ReplyHeader(count = replies.size)
            }

            // 3. 답글 리스트 영역
            items(replies) { reply ->
                ReplyItemCard(
                    item = reply
                )
                // 답글 사이의 얇은 구분선
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFF0F0F0)))
            }
        }
    }
}

@Composable
fun ReplyHeader(count: Int) {
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
fun ReplyItemCard(
    item: PerspectiveUiModel,
    modifier: Modifier = Modifier,
) {
    var isMenuExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp)
    ) {
        // 프로필 헤더 (원문 댓글과 동일)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = item.profileImageRes),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = item.nickname, style = SwypTheme.typography.labelMedium, color = Gray900)
            Spacer(modifier = Modifier.width(6.dp))

            // 딱지
            Box(
                modifier = Modifier
                    .background(SwypTheme.colors.primary.copy(alpha = 0.1f), RoundedCornerShape(2.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(text = item.stance.label, style = SwypTheme.typography.b5Medium, color = SwypTheme.colors.primary)
            }
            Spacer(modifier = Modifier.weight(1f))

            // 케밥 아이콘 (간소화)
            Box {
                IconButton(onClick = { isMenuExpanded = true }, modifier = Modifier.size(24.dp)) {
                    Icon(painterResource(id = R.drawable.ic_more), "더보기", tint = SwypTheme.colors.outline)
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

        Spacer(modifier = Modifier.height(10.dp))

        // 답글 본문
        Text(text = item.content, style = SwypTheme.typography.b4Regular, color = Gray900)

        Spacer(modifier = Modifier.height(10.dp))

        // 하단 영역 (오직 좋아요만 우측 정렬!)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(id = R.drawable.ic_heart_plus), "좋아요", modifier = Modifier.size(16.dp), tint = SwypTheme.colors.outline)
            Spacer(modifier = Modifier.width(4.dp))
            Text("${item.likeCount}", style = SwypTheme.typography.labelMedium, color = SwypTheme.colors.outline)
        }
    }
}