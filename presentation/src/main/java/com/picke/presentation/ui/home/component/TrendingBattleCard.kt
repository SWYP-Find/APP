package com.picke.presentation.ui.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.picke.presentation.R
import com.picke.presentation.ui.component.shimmer
import com.picke.presentation.ui.home.model.HomeContentUiModel
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun TrendingBattleCard(
    item: HomeContentUiModel,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(220.dp)
            .border(1.dp, PickeTheme.colors.borderDefault, RoundedCornerShape(2.dp))
            .background(PickeTheme.colors.surface)
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.2f),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(4.dp, PickeTheme.colors.borderSubtle)
        ) {
            SubcomposeAsyncImage(
                model = item.thumbnailUrl,
                contentDescription = null,
                modifier = Modifier
                    .clip(RoundedCornerShape(2.dp))
                    .background(PickeTheme.colors.backgroundBrand),
                contentScale = ContentScale.Crop,
                loading = {
                    Spacer(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer()
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        Surface(color = PickeTheme.colors.borderDefault, shape = RoundedCornerShape(2.dp)) {
            Text(
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                text = "#${item.tags.firstOrNull() ?: "이슈"}",
                style = PickeTheme.typography.labelXSmall,
                color = PickeTheme.colors.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.title,
            style = PickeTheme.typography.b3SemiBold,
            color = PickeTheme.colors.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = PickeTheme.colors.neutral400
            )

            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = item.timeInfoText,
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.neutral400
            )

            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(id = R.drawable.ic_eye),
                contentDescription = null,
                modifier = Modifier.size(12.dp),
                tint = PickeTheme.colors.neutral400
            )

            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = item.viewCountText,
                style = PickeTheme.typography.label,
                color = PickeTheme.colors.neutral400
            )
        }
    }
}