package com.picke.presentation.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import com.picke.domain.model.SpeakerType
import com.picke.presentation.R
import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel
import com.picke.presentation.ui.theme.SwypTheme

@Composable
fun ChatBubble(
    script: ScenarioScriptUiModel,
    isActive: Boolean,
    showAvatarAndName: Boolean,
    onClick: () -> Unit
) {
    if (script.speakerType == SpeakerType.NARRATOR) {
        val formattedText = script.displayText.replace(Regex(",\\s*"), ",\n")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .alpha(if (isActive) 1f else 0.8f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = formattedText,
                color = if (isActive) SwypTheme.colors.textSecondary else SwypTheme.colors.textMuted,
                style = SwypTheme.typography.label.copy(
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { onClick() }
            )
        }
        return
    }

    val isLeft = script.speakerType == SpeakerType.A
    val bubbleBgColor = if (isLeft) Color.White else SwypTheme.colors.borderDisabled
    val bubbleBorderColor =
        if (isLeft) SwypTheme.colors.borderDisabled else SwypTheme.colors.borderSubtle
    val imageModel = script.profileImageUrl ?: R.drawable.illust_mengzi

    // 구조: [왼쪽 슬롯(36dp)] + [말풍선 영역(남은공간 전부)] + [오른쪽 슬롯(36dp)]
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isActive) 1f else 0.8f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {

        // 🔹 [왼쪽 슬롯] : 왼쪽 화자면 아바타, 오른쪽 화자면 애니메이션
        Box(
            modifier = Modifier
                .width(36.dp)
                .align(if (!isLeft && isActive) Alignment.CenterVertically else Alignment.Top),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLeft && showAvatarAndName) {
                ProfileImage(model = imageModel, modifier = Modifier.size(32.dp))
            } else if (!isLeft && isActive) {
                ChattingLoadingAnimation()
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 🔹 [가운데 영역] : 텍스트 말풍선
        Column(
            modifier = Modifier.weight(1f)
        ) {
            // 이름
            if (showAvatarAndName) {
                Text(
                    text = script.speakerName,
                    style = SwypTheme.typography.b3SemiBold,
                    color = SwypTheme.colors.neutral400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    textAlign = if (isLeft) TextAlign.Start else TextAlign.End
                )
            }

            // 말풍선 본체 (hug: 내용 길이만큼만 너비를 차지하고, 화자 쪽으로 정렬된다)
            Box(
                modifier = Modifier
                    .align(if (isLeft) Alignment.Start else Alignment.End)
                    .clickable { onClick() }
                    .background(bubbleBgColor, RoundedCornerShape(2.dp))
                    .border(1.dp, bubbleBorderColor, RoundedCornerShape(2.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = script.displayText,
                    style = SwypTheme.typography.b5Medium,
                    color = if (isActive) SwypTheme.colors.textSecondary else SwypTheme.colors.textMuted,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))

        // 🔹 [오른쪽 슬롯] : 오른쪽 화자면 아바타, 왼쪽 화자면 애니메이션
        Box(
            modifier = Modifier
                .width(36.dp)
                .align(if (isLeft && isActive) Alignment.CenterVertically else Alignment.Top),
            contentAlignment = Alignment.TopCenter
        ) {
            if (!isLeft && showAvatarAndName) {
                ProfileImage(model = imageModel, modifier = Modifier.size(32.dp))
            } else if (isLeft && isActive) {
                ChattingLoadingAnimation()
            }
        }
    }
}