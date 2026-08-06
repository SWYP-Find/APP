package com.picke.presentation.ui.explore.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.picke.presentation.R
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.explore.model.ExploreUiModel
import com.picke.presentation.ui.theme.PickeTheme
import com.picke.presentation.util.DummyData

@Composable
fun ExploreCard(
    item: ExploreUiModel,
    onClick: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surface)
            .clickable { onClick(item.battleId) }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SubcomposeAsyncImage(
            model = item.thumbnailUrl,
            contentDescription = "Content Thumbnail",
            modifier = Modifier
                .width(80.dp)
                .aspectRatio(3f / 4f)
                .clip(RoundedCornerShape(2.dp)),
            contentScale = ContentScale.Crop,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .shimmer()
                )
            }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            Row(verticalAlignment = Alignment.Top) {
                item.tags.firstOrNull()?.let { category ->
                    Surface(
                        color = PickeTheme.colors.borderDefault,
                        shape = RoundedCornerShape(2.dp)
                    ) {
                        Text(
                            text = "#$category",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = PickeTheme.typography.labelXSmall.copy(fontSize = 12.sp),
                            color = PickeTheme.colors.primary,
                            maxLines = 1
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                Text(
                    text = item.title,
                    style = PickeTheme.typography.b3SemiBold.copy(
                        lineBreak = LineBreak(
                            strategy = LineBreak.Strategy.HighQuality,
                            strictness = LineBreak.Strictness.Loose,
                            wordBreak = LineBreak.WordBreak.Default
                        )
                    ),
                    color = PickeTheme.colors.textTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = item.summary,
                style = PickeTheme.typography.b4Regular,
                color = PickeTheme.colors.neutral400,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = PickeTheme.colors.textMuted
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.audioDurationText,
                        style = PickeTheme.typography.label,
                        color = PickeTheme.colors.neutral400
                    )

                    Spacer(modifier = Modifier.width(12.dp))
                    Icon(
                        painter = painterResource(id = R.drawable.ic_eye),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = PickeTheme.colors.textMuted
                    )

                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = item.viewCountText,
                        style = PickeTheme.typography.label,
                        color = PickeTheme.colors.neutral400
                    )
                }
            }
        }
    }
}o