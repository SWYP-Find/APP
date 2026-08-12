package com.picke.presentation.ui.scenario.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.domain.feature.scenario.model.SpeakerType
import com.picke.presentation.R
import com.picke.presentation.ui.component.ProfileImage
import com.picke.presentation.ui.scenario.model.ScenarioScriptUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

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
                color = if (isActive) PickeTheme.colors.textSecondary else PickeTheme.colors.textMuted,
                style = PickeTheme.typography.label.copy(
                    fontStyle = FontStyle.Italic
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.clickable { onClick() }
            )
        }

        return
    }

    val isLeft = script.speakerType == SpeakerType.A
    val imageModel = script.profileImageUrl ?: R.drawable.illust_mengzi

    val bubbleBgColor =
        if (isLeft) Color.White else PickeTheme.colors.borderDisabled
    val bubbleBorderColor =
        if (isLeft) PickeTheme.colors.borderDisabled else PickeTheme.colors.borderSubtle
    val textColor =
        if (isActive) PickeTheme.colors.textSecondary else PickeTheme.colors.textMuted

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(if (isActive) 1f else 0.8f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .width(36.dp)
                .align(if (!isLeft && isActive) Alignment.CenterVertically else Alignment.Top),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLeft && showAvatarAndName) {
                ProfileImage(
                    model = imageModel,
                    modifier = Modifier.size(32.dp)
                )
            } else if (!isLeft && isActive) {
                ChattingLoadingAnimation()
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            if (showAvatarAndName) {
                Text(
                    text = script.speakerName,
                    style = PickeTheme.typography.b3SemiBold,
                    color = PickeTheme.colors.neutral400,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 6.dp),
                    textAlign = if (isLeft) TextAlign.Start else TextAlign.End
                )
            }

            Box(
                modifier = Modifier
                    .align(if (isLeft) Alignment.Start else Alignment.End)
                    .clickable { onClick() }
                    .background(
                        color = bubbleBgColor,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = bubbleBorderColor,
                        shape = RoundedCornerShape(2.dp)
                    )
                    .padding(12.dp)
            ) {
                Text(
                    text = script.displayText,
                    style = PickeTheme.typography.b5Medium,
                    color = textColor,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier
                .width(36.dp)
                .align(if (isLeft && isActive) Alignment.CenterVertically else Alignment.Top),
            contentAlignment = Alignment.TopCenter
        ) {
            if (!isLeft && showAvatarAndName) {
                ProfileImage(
                    model = imageModel,
                    modifier = Modifier.size(32.dp)
                )
            } else if (isLeft && isActive) {
                ChattingLoadingAnimation()
            }
        }
    }
}

@Composable
private fun ChattingLoadingAnimation(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_bars_5")
    val baseHeights = listOf(6.dp, 10.dp, 15.dp, 10.dp, 6.dp)
    val maxScales = listOf(1.1f, 1.3f, 1.6f, 1.3f, 1.1f)

    val animations = (0 until 5).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.6f,
            targetValue = maxScales[index],
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 400,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(offsetMillis = index * 120)
            ),
            label = "scale_${index + 1}"
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier
    ) {
        repeat(5) { index ->
            Box(
                modifier = Modifier
                    .size(
                        width = 2.5.dp,
                        height = baseHeights[index]
                    )
                    .scale(
                        scaleX = 1f,
                        scaleY = animations[index].value
                    )
                    .background(
                        color = Color(0xFF8D4B38),
                        shape = RoundedCornerShape(50)
                    )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChatBubblePreview() {
    PickeTheme {
        ChatBubble(
            script = DummyData.dummyScripts.first(),
            isActive = true,
            showAvatarAndName = true,
            onClick = {}
        )
    }
}