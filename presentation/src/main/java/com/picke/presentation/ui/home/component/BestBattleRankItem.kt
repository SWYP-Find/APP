package com.picke.presentation.ui.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.picke.presentation.R
import com.picke.presentation.ui.home.model.HomeContentUiModel
import com.picke.presentation.ui.theme.PickeTheme

@Composable
fun BestBattleRankItem(
    item: HomeContentUiModel,
    rank: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.Top
        ) {
            val rankColor =
                if (rank == 1) PickeTheme.colors.primary else if (rank == 2) PickeTheme.colors.secondary else PickeTheme.colors.borderSubtle

            Text(
                text = rank.toString(),
                style = PickeTheme.typography.h1SemiBold,
                color = rankColor,
                modifier = Modifier
                    .fillMaxHeight()
                    .width(24.dp)
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Surface(color = PickeTheme.colors.borderDefault, shape = RoundedCornerShape(2.dp)) {
                    Text(
                        text = "${item.leftProfileName ?: "A"} VS ${item.rightProfileName ?: "B"}",
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        style = PickeTheme.typography.labelXSmall,
                        color = PickeTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = item.title,
                    style = PickeTheme.typography.b3SemiBold,
                    color = PickeTheme.colors.textPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = item.tags.joinToString(" ") { "#$it" },
                        style = PickeTheme.typography.label,
                        color = PickeTheme.colors.textMuted
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painterResource(R.drawable.ic_clock),
                            null,
                            Modifier.size(14.dp),
                            tint = PickeTheme.colors.textMuted
                        )

                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = item.timeInfoText,
                            style = PickeTheme.typography.label,
                            color = PickeTheme.colors.neutral400
                        )

                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            painterResource(R.drawable.ic_eye),
                            null,
                            Modifier.size(14.dp),
                            tint = PickeTheme.colors.textMuted
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
        }
        HorizontalDivider(color = PickeTheme.colors.surfaceTertiary, thickness = 1.dp)
    }
}