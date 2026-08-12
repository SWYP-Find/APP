package com.picke.presentation.ui.onboarding.component

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun ThirdOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .border(1.dp, PickeTheme.colors.borderSubtle, RoundedCornerShape(2.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = PickeTheme.colors.surfaceTertiary,
                        shape = RoundedCornerShape(2.dp)
                    ) {
                        Text(
                            text = "#투표",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = PickeTheme.typography.b4Regular,
                            color = PickeTheme.colors.primary
                        )
                    }
                    Text(
                        text = "985명 참여",
                        style = PickeTheme.typography.label,
                        color = PickeTheme.colors.neutral400
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "가상국가를 만든다면 대통령은 ",
                            style = PickeTheme.typography.b3SemiBold,
                            color = PickeTheme.colors.textPrimary
                        )
                        Box(
                            modifier = Modifier
                                .width(40.dp)
                                .height(20.dp)
                                .border(
                                    1.dp,
                                    PickeTheme.colors.borderDefault,
                                    RoundedCornerShape(2.dp)
                                )
                                .background(PickeTheme.colors.surfaceSubtle)
                        )
                        Text(
                            text = "이다",
                            style = PickeTheme.typography.b3SemiBold,
                            color = PickeTheme.colors.textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "빈칸에 들어갈 가장 적절한 답을 골라주세요",
                        style = PickeTheme.typography.label,
                        color = PickeTheme.colors.textMuted,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThirdOnboardingButton(
                                modifier = Modifier.weight(1f),
                                index = "1",
                                text = "석가모니"
                            )
                            ThirdOnboardingButton(
                                modifier = Modifier.weight(1f),
                                index = "2",
                                text = "세종대왕"
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            ThirdOnboardingButton(
                                modifier = Modifier.weight(1f),
                                index = "3",
                                text = "예수"
                            )
                            ThirdOnboardingButton(
                                modifier = Modifier.weight(1f),
                                index = "4",
                                text = "일론 머스크"
                            )
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

@Composable
fun ThirdOnboardingButton(
    index: String,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surfaceSubtle)
            .border(1.dp, PickeTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$index. ",
            style = PickeTheme.typography.labelXSmall,
            color = Color(0xFFCBA572)
        )
        Text(
            text = text,
            style = PickeTheme.typography.label,
            color = PickeTheme.colors.textPrimary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ThirdOnboardingCardPreview() {
    PickeTheme { ThirdOnboardingCard() }
}