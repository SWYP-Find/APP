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
    // NARRATOR
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

    val isLeft = script.speakerType == SpeakerType.A

    val bubbleBgColor = if (isLeft) Color.White else Beige500
    val bubbleBorderColor = if (isLeft) Beige500 else Beige700

    val imageModel = script.profileImageUrl ?: R.drawable.ic_profile_mengzi

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isLeft) Arrangement.Start else Arrangement.End,
        verticalAlignment = Alignment.Top
    ) {
        // 왼쪽 화자 프로필
        if (isLeft) {
            if (showAvatarAndName) {
                ProfileImage(model = imageModel, modifier = Modifier.size(32.dp))
            } else {
                Spacer(modifier = Modifier.width(32.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        // 오른쪽 화자 파형 애니메이션
        if (!isLeft && isActive) {
            Box(modifier = Modifier.padding(8.dp)) {
                ChattingLoadingAnimation()
            }
        }

        // 이름 & 채팅
        Column(
            modifier = Modifier.weight(1f, fill = false),
            horizontalAlignment = if (isLeft) Alignment.Start else Alignment.End
        ) {
            if (showAvatarAndName) {
                Text(
                    text = script.speakerName,
                    style = SwypTheme.typography.b3SemiBold,
                    color = Gray400,
                    modifier = Modifier.padding(2.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .background(bubbleBgColor, RoundedCornerShape(2.dp))
                    .border(1.dp, bubbleBorderColor, RoundedCornerShape(2.dp))
                    .padding(8.dp)
            ) {
                Text(
                    text = script.displayText,
                    style = SwypTheme.typography.b5Medium,
                    color = Gray300
                )
            }
        }

        // 왼쪽 화자 파형 애니메이션
        if (isLeft && isActive) {
            Box(modifier = Modifier.padding(8.dp)) {
                ChattingLoadingAnimation()
            }
        }

        // 오른쪽 화자 프로필
        if (!isLeft) {
            Spacer(modifier = Modifier.width(8.dp))
            if (showAvatarAndName) {
                ProfileImage(model = imageModel, modifier = Modifier.size(32.dp))
            } else {
                Spacer(modifier = Modifier.width(32.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}