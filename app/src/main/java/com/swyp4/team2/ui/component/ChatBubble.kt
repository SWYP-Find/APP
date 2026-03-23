package com.swyp4.team2.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.swyp4.team2.domain.model.SpeakerType
import com.swyp4.team2.ui.scenario.model.ScenarioScriptUiModel
import com.swyp4.team2.ui.theme.*
import com.swyp4.team2.R

@Composable
fun ChatBubble(
    script: ScenarioScriptUiModel,
    isActive: Boolean,
    showAvatarAndName: Boolean
) {
    // 1. 나레이터는 화면 중앙에 텍스트만 표시
    if (script.speakerType == SpeakerType.NARRATOR) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp).alpha(if (isActive) 1f else 0.5f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = script.displayText,
                color = Gray500,
                style = SwypTheme.typography.label,
                textAlign = TextAlign.Center
            )
        }
        return
    }

    // 2. A는 왼쪽, B는 오른쪽
    val isLeft = script.speakerType == SpeakerType.A

    // 3. 색상: 왼쪽은 흰색 바탕, 오른쪽은 베이지 바탕
    val bubbleBgColor = if (isLeft) Color.White else Beige500
    val bubbleBorderColor = if (isLeft) Beige50 else Beige700

    // 임시 프로필 이미지 매핑
    val profileRes = if (isLeft) R.drawable.ic_profile_mengzi else R.drawable.ic_profile_xunzi
    // TODO image 백엔드 한테 api로 달라고 하기

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isLeft) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        // --- [왼쪽 화자 프로필] ---
        if (isLeft) {
            if (showAvatarAndName) {
                ProfileImage(model = profileRes, modifier = Modifier.size(40.dp))
            } else {
                Spacer(modifier = Modifier.width(40.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // --- [오른쪽 화자 파형 애니메이션] ---
        if (!isLeft && isActive) {
            Box(modifier = Modifier.padding(top = 12.dp, end = 8.dp)) {
                ChattingLoadingAnimation()
            }
        }

        // --- [말풍선 본체 영역] ---
        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End
        ) {
            // 이름
            if (showAvatarAndName) {
                Text(
                    text = script.speakerName,
                    style = SwypTheme.typography.b3SemiBold,
                    color = Gray700,
                    modifier = Modifier.padding(bottom = 6.dp, start = 4.dp, end = 4.dp)
                )
            }

            // 말풍선 박스
            Box(
                modifier = Modifier
                    .background(bubbleBgColor, RoundedCornerShape(2.dp))
                    .border(1.dp, bubbleBorderColor, RoundedCornerShape(2.dp))
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                Text(
                    text = script.displayText,
                    style = SwypTheme.typography.b3Regular,
                    color = Gray900,
                    lineHeight = SwypTheme.typography.b3Regular.lineHeight
                )
            }
        }

        // --- [왼쪽 화자 (플라톤) 파형 애니메이션] ---
        if (isLeft && isActive) {
            Box(modifier = Modifier.padding(top = 12.dp, start = 8.dp)) {
                ChattingLoadingAnimation()
            }
        }

        // --- [오른쪽 화자 (사르트르) 프로필] ---
        if (!isLeft) {
            Spacer(modifier = Modifier.width(8.dp))
            if (showAvatarAndName) {
                ProfileImage(model = profileRes, modifier = Modifier.size(40.dp))
            } else {
                Spacer(modifier = Modifier.width(40.dp)) // 프로필 안 보일 때도 공간 차지
            }
        }
    }
}