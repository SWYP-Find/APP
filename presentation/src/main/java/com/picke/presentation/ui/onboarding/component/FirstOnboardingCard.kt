package com.picke.presentation.ui.onboarding.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun FirstOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FirstOnboardingChatBubble(
                profileResId = R.drawable.illust_kant,
                name = "칸트",
                message = "인간은 짐승과 달리 스스로 세운 도덕 법칙에 복종할 수 있는 '이성적 존재'입니다.",
                isLeft = true
            )

            FirstOnboardingChatBubble(
                profileResId = R.drawable.illust_nietzsche,
                name = "니체",
                message = "이성이요? 당신은 그 차가운 이성으로 생동감 넘치는 삶의 본능을 죽이고 있습니다.",
                isLeft = false
            )

            FirstOnboardingChatBubble(
                profileResId = R.drawable.illust_kant,
                name = "칸트",
                message = "삶의 목적은 '행복'이 아니라 '행복해질 자격'을 갖추는 것입니다. 그것은 도덕적 의무를 완수하는 삶이죠.",
                isLeft = true
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .align(Alignment.BottomCenter)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0f),
                            PickeTheme.colors.surfaceSubtle
                        )
                    )
                )
        )
    }
}

@Composable
fun FirstOnboardingChatBubble(
    profileResId: Int,
    name: String,
    message: String,
    isLeft: Boolean,
    modifier: Modifier = Modifier
) {
    val bubbleBgColor = if (isLeft) Color.White else PickeTheme.colors.borderDisabled
    val bubbleBorderColor =
        if (isLeft) PickeTheme.colors.borderDisabled else PickeTheme.colors.borderSubtle

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.width(36.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (isLeft) {
                Image(
                    painter = painterResource(id = profileResId),
                    contentDescription = name,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                style = PickeTheme.typography.b3SemiBold,
                color = PickeTheme.colors.neutral400,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 6.dp),
                textAlign = if (isLeft) TextAlign.Start else TextAlign.End
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(bubbleBgColor, RoundedCornerShape(2.dp))
                    .border(1.dp, bubbleBorderColor, RoundedCornerShape(2.dp))
                    .padding(12.dp)
            ) {
                Text(
                    text = message,
                    style = PickeTheme.typography.b5Medium,
                    color = PickeTheme.colors.textSecondary,
                    textAlign = TextAlign.Start
                )
            }
        }

        Spacer(modifier = Modifier.width(8.dp))
        Box(
            modifier = Modifier.width(36.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (!isLeft) {
                Image(
                    painter = painterResource(id = profileResId),
                    contentDescription = name,
                    modifier = Modifier.size(32.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FirstOnboardingCardPreview() {
    PickeTheme { FirstOnboardingCard() }
}