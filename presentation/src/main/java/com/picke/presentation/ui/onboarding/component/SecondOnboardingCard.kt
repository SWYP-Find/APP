package com.picke.presentation.ui.onboarding.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun SecondOnboardingCard(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(2.dp))
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            SecondOnboardingHeader()

            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SecondOnboardingItemCard(
                    profileResId = R.drawable.illust_raccoon,
                    nickname = "사유하는 라쿤",
                    replyCount = "23",
                    likeCount = "1,340"
                )
                SecondOnboardingItemCard(
                    profileResId = R.drawable.illust_dochi,
                    nickname = "사유하는 고슴도치",
                    replyCount = "0",
                    likeCount = "0"
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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
fun SecondOnboardingHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Surface(color = PickeTheme.colors.primaryLight, shape = RoundedCornerShape(4.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_think),
                        contentDescription = null,
                        tint = PickeTheme.colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "생각이 바뀌었어요",
                        style = PickeTheme.typography.caption2SemiBold,
                        color = PickeTheme.colors.primary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "A 78.0%",
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.neutral600
            )

            Spacer(modifier = Modifier.width(12.dp))
            Row(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
            ) {
                Box(
                    modifier = Modifier
                        .weight(0.78f)
                        .fillMaxHeight()
                        .background(Color(0xFFA64D47))
                )
                Box(
                    modifier = Modifier
                        .weight(0.22f)
                        .fillMaxHeight()
                        .background(PickeTheme.colors.backgroundTertiary)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "B 22.0%",
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.neutral600
            )
        }
    }
}

@Composable
fun SecondOnboardingItemCard(
    profileResId: Int,
    nickname: String,
    replyCount: String,
    likeCount: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(2.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(
            width = 1.dp,
            color = PickeTheme.colors.borderDefault
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = profileResId),
                    contentDescription = null,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = nickname,
                            style = PickeTheme.typography.labelMedium,
                            color = PickeTheme.colors.textSecondary
                        )

                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            color = PickeTheme.colors.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(2.dp)
                        ) {
                            Text(
                                text = "A",
                                style = PickeTheme.typography.b5Medium,
                                color = PickeTheme.colors.primary,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Text(
                        text = "2분 전",
                        style = PickeTheme.typography.labelXSmall,
                        color = PickeTheme.colors.outline
                    )
                }

                Icon(
                    painter = painterResource(id = R.drawable.ic_more),
                    contentDescription = null,
                    tint = PickeTheme.colors.textMuted,
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "제도화가 무서운 건, 사회적 압력이 '선택'을 '의무'로 바꿀 수 있다는 거예요. 네덜란드 사례를 보면 우려가 현실이 되고 있죠.",
                style = PickeTheme.typography.b4Regular,
                color = PickeTheme.colors.neutral600,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(12.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "더보기",
                    style = PickeTheme.typography.b5Medium,
                    color = PickeTheme.colors.textMuted
                )

                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = R.drawable.ic_message),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = PickeTheme.colors.textMuted
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = replyCount,
                    style = PickeTheme.typography.b5Medium,
                    color = PickeTheme.colors.textMuted
                )

                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    painter = painterResource(id = R.drawable.ic_heart_plus),
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = PickeTheme.colors.textMuted
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = likeCount,
                    style = PickeTheme.typography.b5Medium,
                    color = PickeTheme.colors.textMuted
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SecondOnboardingCardPreview() {
    PickeTheme { SecondOnboardingCard() }
}