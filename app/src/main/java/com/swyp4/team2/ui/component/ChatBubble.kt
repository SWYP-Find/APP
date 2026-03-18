package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.swyp4.team2.R
import com.swyp4.team2.domain.model.DebateMessage
import com.swyp4.team2.domain.model.SpeakerType
import com.swyp4.team2.ui.debate.DebateMessageLocal
import com.swyp4.team2.ui.debate.SpeakerTypeLocal
import com.swyp4.team2.ui.theme.*

@Composable
fun ChatBubble(
    message: DebateMessageLocal,
    isActive: Boolean,
    showAvatarAndName: Boolean
) {
    val isLeft = message.speakerType == SpeakerTypeLocal.LEFT

    // 피그마 디자인에 맞춘 색상
    val bubbleBgColor = if (isLeft) Color.White else Beige50
    val bubbleBorderColor = if (isLeft) Gray200 else Beige400

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isLeft) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        // [오른쪽 화자일 경우] 왼쪽에 제시카님이 만든 파형 애니메이션 띄우기!
        if (!isLeft && isActive) {
            Box(modifier = Modifier.padding(top = 16.dp)) {
                ChattingLoadingAnimation()
            }
        }

        // [왼쪽 화자 프로필 영역]
        if (isLeft) {
            ProfileArea(
                profileRes = message.profileRes, // 🌟 이제 모델에서 이미지를 직접 가져옵니다!
                speakerName = message.speakerName,
                showAvatar = showAvatarAndName
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        // [말풍선 & 이름 영역]
        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End
        ) {
            // 이름 (연속되지 않은 첫 메시지일 때만 표시)
            if (showAvatarAndName) {
                Text(
                    text = message.speakerName,
                    style = SwypTheme.typography.labelMedium,
                    color = Gray900,
                    modifier = Modifier.padding(bottom = 4.dp, start = 4.dp, end = 4.dp)
                )
            }

            // 말풍선 박스
            Box(
                modifier = Modifier
                    .background(bubbleBgColor, RoundedCornerShape(2.dp))
                    .border(1.dp, bubbleBorderColor, RoundedCornerShape(2.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = message.text,
                    style = SwypTheme.typography.b3Regular,
                    color = if (isActive) Gray900 else Gray500
                )
            }
        }

        // [오른쪽 화자 프로필 영역]
        if (!isLeft) {
            Spacer(modifier = Modifier.width(8.dp))
            ProfileArea(
                profileRes = message.profileRes,
                speakerName = message.speakerName,
                showAvatar = showAvatarAndName
            )
        }

        // [왼쪽 화자일 경우] 오른쪽에 제시카님이 만든 파형 애니메이션 띄우기!
        if (isLeft && isActive) {
            ChattingLoadingAnimation(
                modifier = Modifier.padding(top = 16.dp, start = 12.dp)
            )
        }
    }
}

// 프로필 그리기 컴포넌트
@Composable
fun ProfileArea(profileRes: Int, speakerName: String, showAvatar: Boolean) {
    Box(modifier = Modifier.size(40.dp)) {
        if (showAvatar) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Gray100),
                contentAlignment = Alignment.Center
            ) {
                // 실제 프로필 아이콘이 있다면 이걸 사용하세요!
                Icon(
                    painter = painterResource(id = profileRes),
                    contentDescription = speakerName,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
