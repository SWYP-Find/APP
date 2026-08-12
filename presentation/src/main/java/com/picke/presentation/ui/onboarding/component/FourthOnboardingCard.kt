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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun FourthOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp)
                .border(1.dp, PickeTheme.colors.surfaceTertiary, RoundedCornerShape(2.dp))
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(PickeTheme.colors.primary)
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "나의 철학자 유형",
                        style = PickeTheme.typography.labelMedium,
                        color = PickeTheme.colors.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "칸트형",
                        style = PickeTheme.typography.h3SemiBold,
                        color = PickeTheme.colors.neutral600
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.illust_kant),
                        contentDescription = "칸트 프로필",
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(PickeTheme.colors.beige100)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "결과보다 과정을 중시하고, 보편적 도덕 법칙을 따르는 원칙주의자. 어떤 상황에서도 흔들리지 않는 기준을 가진 사람입니다.",
                        style = PickeTheme.typography.b5Medium,
                        color = PickeTheme.colors.neutral600,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val tags = listOf("의무론", "규칙 중시", "보편 원칙", "이상적")
                        tags.forEach { tag ->
                            Box(
                                modifier = Modifier
                                    .border(
                                        1.dp,
                                        PickeTheme.colors.borderDefault,
                                        RoundedCornerShape(2.dp)
                                    )
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = tag,
                                    style = PickeTheme.typography.labelXSmall,
                                    color = Color(0xFF8C3E26)
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
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

@Preview(showBackground = true)
@Composable
fun FourthOnboardingCardPreview() {
    PickeTheme { FourthOnboardingCard() }
}