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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.swyp4.team2.ui.debate.DebateMessageLocal
import com.swyp4.team2.ui.debate.SpeakerTypeLocal
import com.swyp4.team2.ui.theme.*

@Composable
fun ChatBubble(
    message: DebateMessageLocal,
    isActive: Boolean,
    showAvatarAndName: Boolean
) {
    if (message.speakerType == SpeakerTypeLocal.CENTER) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message.text,
                color = Gray500,
                style = SwypTheme.typography.label,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    val isLeft = message.speakerType == SpeakerTypeLocal.LEFT

    // 피그마 디자인에 맞춘 색상
    val bubbleBgColor = if (isLeft) Color.White else Beige50
    val bubbleBorderColor = if (isLeft) Gray200 else Beige400

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isLeft) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top // 🌟 2. 프로필을 상단에 맞춥니다.
    ) {
        // [오른쪽 화자일 경우] 왼쪽에 파형 애니메이션 띄우기
        if (!isLeft && isActive) {
            Box(modifier = Modifier.align(Alignment.Bottom).padding(bottom = 12.dp, end = 8.dp)) { // 🌟 3. 파형만 바닥에 고정!
                ChattingLoadingAnimation()
            }
        }

        // [왼쪽 화자 프로필 영역]
        if (isLeft) {
            ProfileImage(
                model = message.profileRes,
                modifier = Modifier.size(40.dp)
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
                    text = message.text, // 🚨 ViewModel에서 SSML 태그(<speak> 등)를 꼭 제거해서 넘겨주셔야 합니다!
                    style = SwypTheme.typography.b3Regular,
                    color = if (isActive) Gray900 else Gray500
                )
            }
        }

        // [오른쪽 화자 프로필 영역]
        if (!isLeft) {
            Spacer(modifier = Modifier.width(8.dp))
            ProfileImage(
                model = message.profileRes,
                modifier = Modifier.size(40.dp)
            )
        }

        // [왼쪽 화자일 경우] 오른쪽에 파형 애니메이션 띄우기
        if (isLeft && isActive) {
            Box(modifier = Modifier.align(Alignment.Bottom).padding(bottom = 12.dp, start = 8.dp)) { // 🌟 3. 파형만 바닥에 고정!
                ChattingLoadingAnimation()
            }
        }
    }
}
