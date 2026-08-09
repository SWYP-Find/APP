package com.picke.presentation.ui.recommend.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.component.BattleOpinionBox
import com.picke.presentation.ui.recommend.model.RecommendUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

@Composable
fun RecommendItemCard(
    item: RecommendUiModel,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surface)
            .border(1.dp, PickeTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(color = PickeTheme.colors.borderDefault, shape = RoundedCornerShape(2.dp)) {
                Text(
                    text = "#${item.tags.firstOrNull() ?: "이슈"}",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    style = PickeTheme.typography.label,
                    color = PickeTheme.colors.primary
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painterResource(R.drawable.ic_clock),
                    null,
                    Modifier.size(12.dp),
                    tint = PickeTheme.colors.neutral400
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.audioDuration}분",
                    style = PickeTheme.typography.label,
                    color = PickeTheme.colors.neutral400
                )

                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painterResource(R.drawable.ic_eye),
                    null,
                    Modifier.size(12.dp),
                    tint = PickeTheme.colors.neutral400
                )

                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${item.viewCount}",
                    style = PickeTheme.typography.label,
                    color = PickeTheme.colors.neutral400
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = item.title,
            style = PickeTheme.typography.b3SemiBold,
            color = PickeTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.summary,
            style = PickeTheme.typography.label,
            color = PickeTheme.colors.neutral400,
            maxLines = 2,
            minLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            BattleOpinionBox(
                modifier = Modifier.weight(1f),
                opinion = item.stanceA,
                name = item.representativeA,
                imageUrl = item.imageA
            )
            Surface(
                modifier = Modifier
                    .size(40.dp)
                    .padding(6.dp),
                shape = CircleShape,
                color = PickeTheme.colors.secondaryLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "VS",
                        style = PickeTheme.typography.labelXSmall,
                        color = PickeTheme.colors.textPrimary
                    )
                }
            }
            BattleOpinionBox(
                modifier = Modifier.weight(1f),
                opinion = item.stanceB,
                name = item.representativeB,
                imageUrl = item.imageB
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RecommendItemCardPreview() {
    PickeTheme {
        RecommendItemCard(
            item = DummyData.dummyRecommends.first(),
            onClick = {}
        )
    }
}